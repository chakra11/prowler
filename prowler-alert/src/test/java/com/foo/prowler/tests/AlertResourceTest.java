package com.foo.prowler.tests;

import com.foo.prowler.api.Alert;
import com.foo.prowler.api.RedactedContent;
import com.foo.prowler.api.SensitiveDataType;
import com.foo.prowler.api.SensitiveTypeName;
import com.foo.prowler.app.AlertApplication;
import com.foo.prowler.config.AlertConfiguration;
import com.foo.prowler.dao.AlertDAO;
import com.foo.prowler.resources.AlertResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertResourceTest {
    @Mock
    private static AlertDAO alertDAO;

    @ClassRule
    public static final ResourceTestRule resources =
            ResourceTestRule.builder().addResource(new AlertResource(alertDAO)).build();

    private final Alert alert = new Alert(
            LocalDateTime.now(),
            "host1",
            "application1",
            "location1",
            new RedactedContent(
                    "my credit card number is xxxx-xxxx-xxxx-xxxx",
                    new ArrayList<SensitiveDataType>() {{
                        add(new SensitiveDataType(
                                SensitiveTypeName.CREDIT_CARD_NUMBER, "credit card number"));
                    }}
            )
    );

    @BeforeEach
    void setup() {
        List<Alert> alerts = new ArrayList<>(){{
            add(alert);
        }};

        when(alertDAO.findAll()).thenReturn(alerts);
        when(alertDAO.findById(eq((long)1))).thenReturn(alert);
    }

    @After
    void tearDown() {
        reset(alertDAO);
    }

    @Test
    void testFindById() {
        Alert alert = Alert.class.cast(resources.target("/alerts/1").request().get().getEntity());
        assert(alert.getHostname().equals("host1"));
        assert(alert.getApplication().equals("application1"));
        assert(alert.getLocation().equals("location1"));
        assert(alert.getRedactedContent().getSensitiveDataTypes().get(0).equals(SensitiveTypeName.CREDIT_CARD_NUMBER));
    }
}
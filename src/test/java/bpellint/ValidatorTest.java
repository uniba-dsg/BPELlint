package bpellint;

import bpellint.core.validators.rules.Validator;
import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void testConversion() {
        Assert.assertEquals("SA00001", Validator.numberToSAConstraint(1));
        Assert.assertEquals("SA00011", Validator.numberToSAConstraint(11));
    }

}

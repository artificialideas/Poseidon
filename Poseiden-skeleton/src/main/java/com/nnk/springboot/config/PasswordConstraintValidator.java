package com.nnk.springboot.config;

import com.google.common.base.Joiner;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword arg0) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // @formatter:off
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 60),                               // Between 8 and 60 char (Bcrypt generates a 60 char hash)
                new UppercaseCharacterRule(1),                  // At least 1 upper-case char
                new DigitCharacterRule(1),                      // At least 1 digit
                new SpecialCharacterRule(1),                    // At least 1 symbol
                new WhitespaceRule()));                              // No whitespace
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

//        List<String> messages = validator.getMessages(result);
//        String messageTemplate = String.join(", ", messages);
//        context.buildConstraintViolationWithTemplate(messageTemplate)
//                .addConstraintViolation()
//                .disableDefaultConstraintViolation();
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result))).addConstraintViolation();
        return false;
    }
}

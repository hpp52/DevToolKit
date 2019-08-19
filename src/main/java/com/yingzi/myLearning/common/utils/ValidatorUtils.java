package com.dfkj.myLearning.common.utils;

import com.dfkj.center.bio.entity.exception.BioPlatformException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

public class ValidatorUtils {
	private static Logger log = LoggerFactory.getLogger(ValidatorUtils.class);
	private static Validator validator;

	public static <T> void validate(T obj) {
		Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(obj);
		if (CollectionUtils.isEmpty(constraintViolations)) {
			return;
		}
		StringBuffer paramMsg = new StringBuffer();
		constraintViolations.forEach(violation -> paramMsg.append(violation.getMessage()).append(";"));
		log.info("参数校验失败：{}",paramMsg.toString());
		throw new BioPlatformException(paramMsg.toString());
	}

	private static Validator getValidator() {
		if (Objects.isNull(validator)) {
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			validator = validatorFactory.getValidator();
		}
		return validator;
	}
}

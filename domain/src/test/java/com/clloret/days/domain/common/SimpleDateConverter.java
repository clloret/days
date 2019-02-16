package com.clloret.days.domain.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.Converter;
import junitparams.converters.Param;

public class SimpleDateConverter implements Converter<Param, Date> {

  @Override
  public void initialize(Param annotation) {

  }

  @Override
  public Date convert(Object param) throws ConversionFailedException {

    try {
      return new SimpleDateFormat("dd.MM.yyyy").parse(param.toString());
    } catch (ParseException e) {
      throw new ConversionFailedException("failed");
    }
  }
}
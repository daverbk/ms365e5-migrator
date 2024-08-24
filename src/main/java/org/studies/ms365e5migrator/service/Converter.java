package org.studies.ms365e5migrator.service;

public interface Converter<T> {

  T convert(T entity);
}

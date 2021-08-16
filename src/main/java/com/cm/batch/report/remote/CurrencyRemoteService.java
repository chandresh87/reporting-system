package com.cm.batch.report.remote;

/** @author chandresh.mishra */
public interface CurrencyRemoteService {

  CurrencyRatesDTO getCurrencyRates(String baseCurrency);

  void evictAllCacheValues();
}

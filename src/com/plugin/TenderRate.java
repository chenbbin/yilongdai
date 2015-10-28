package com.plugin;

public class TenderRate
  implements Comparable<TenderRate>
{
  private String tenderId;
  private Double rate;
  private String cryid;
  private Double remaining;
  
  public TenderRate(String tenderId, double rate, String cryid, double remaining)
  {
    this.tenderId = tenderId;
    this.rate = Double.valueOf(rate);
    this.cryid = cryid;
    this.remaining = Double.valueOf(remaining);
  }
  
  public String getTenderId() {
    return this.tenderId;
  }
  
  public void setTenderId(String tenderId) {
    this.tenderId = tenderId;
  }
  
  public Double getRate() {
    return this.rate;
  }
  
  public void setRate(Double rate) {
    this.rate = rate;
  }
  
  public String getCryid() {
    return this.cryid;
  }
  
  public void setCryid(String cryid) {
    this.cryid = cryid;
  }
  
  public Double getRemaining() {
    return this.remaining;
  }
  
  public void setRemaining(Double remaining) {
    this.remaining = remaining;
  }
  
  public int compareTo(TenderRate o)
  {
    return o.getRemaining().compareTo(getRemaining());
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\TenderRate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
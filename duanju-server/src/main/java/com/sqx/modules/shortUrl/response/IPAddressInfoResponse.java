package com.sqx.modules.shortUrl.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class IPAddressInfoResponse implements Serializable {
    private static final long serialVersionUID = -4810629646655493342L;

    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
    private String query;
}

package com.social.backend.payloads;

import java.util.Date;
import java.util.List;

public class RateLimitStatus {
    int bucketLimit;
    int currentBucketSize;

    public RateLimitStatus(int bucketLimit, int currentBucketSize) {
        this.bucketLimit = bucketLimit;
        this.currentBucketSize = currentBucketSize;
    }
}

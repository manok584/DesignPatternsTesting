package com.practise.seleniumdesign.test.decorator;

import com.google.common.util.concurrent.Uninterruptibles;
import com.practise.seleniumdesign.decorator.PaymentScreenPage;
import com.practise.seleniumdesign.test.BaseTest;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.practise.seleniumdesign.test.decorator.PaymentDecorators.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PaymentScreenTest extends BaseTest {

    private PaymentScreenPage paymentScreenPage;

    @BeforeTest
    public void setPaymentScreenPage(){
        this.paymentScreenPage = new PaymentScreenPage(driver);
    }

    @Test(dataProvider = "getData")
    public void paymentScreenTest(Consumer<PaymentScreenPage> consumer) {
        paymentScreenPage.goTo();
        consumer.accept(paymentScreenPage);

        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }

    @DataProvider
    public Object[] getData(){
        return new Object[]{
                validCC.andThen(buy).andThen(successfulPurchase),
                freeCoupon.andThen(buy).andThen(successfulPurchase),
                discountedCoupon.andThen(validCC).andThen(buy).andThen(successfulPurchase),
                invalidCC.andThen(buy).andThen(failedPurchase),
                invalidCC.andThen(discountedCoupon).andThen(buy).andThen(failedPurchase),
                buy.andThen(failedPurchase)
        };
    }

}

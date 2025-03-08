package com.codemaniac.appointment.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripePaymentService {

  @Value("${stripe.api.key}")
  private String stripeSecretKey;

  public StripePaymentService(@Value("${stripe.secret.key}") final String stripeSecretKey) {
    this.stripeSecretKey = stripeSecretKey;
    Stripe.apiKey = stripeSecretKey;  // Set once during object creation
  }

  /**
   * Creates a Stripe Payment Intent.
   *
   * @param amount The amount to charge (in cents).
   * @param currency The currency (e.g., "usd", "eur").
   * @return The client secret for the frontend.
   */



  public String createPaymentIntent(final long amount, final String currency)
      throws StripeException {

    final PaymentIntentCreateParams params =
        PaymentIntentCreateParams.builder().setAmount(amount).setCurrency(currency).build();

    final PaymentIntent paymentIntent = PaymentIntent.create(params);
    return paymentIntent.getClientSecret(); // This is sent to the frontend
  }

  /**
   * Confirms payment by verifying its status.
   *
   * @param paymentIntentId The Stripe Payment Intent ID.
   * @return true if payment is successful.
   */
  public boolean confirmPayment(final String paymentIntentId) throws StripeException {

    final PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
    return "succeeded".equals(paymentIntent.getStatus());
  }

  public boolean confirmAndCharge(final String paymentIntentId, final String paymentMethodId)
      throws StripeException {

    final PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

    paymentIntent.update(
        PaymentIntentUpdateParams.builder().setPaymentMethod(paymentMethodId).build());

    paymentIntent.confirm(PaymentIntentConfirmParams.builder().build());

    return "succeeded".equals(paymentIntent.getStatus());
  }
}

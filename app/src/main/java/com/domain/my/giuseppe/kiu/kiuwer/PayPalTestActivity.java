package com.domain.my.giuseppe.kiu.kiuwer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;

public class PayPalTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PayPal pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
                PayPal.ENV_SANDBOX);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout layoutSimplePayment = new RelativeLayout(this);
        layoutSimplePayment.setBackgroundColor(getResources().getColor(R.color.primary));
        layoutSimplePayment.setLayoutParams(params);
        //LayoutSimplePayment.setLayoutParams(new RelativeLayout.LayoutParams(
         //       LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        CheckoutButton launchSimplePayment = pp.getCheckoutButton(this,
                PayPal.BUTTON_152x33, CheckoutButton.TEXT_PAY);

        launchSimplePayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PayPalPayment payment = new PayPalPayment();
                //RICEVO SOMMA DA PAGARE E MAIL DEL DESTINATARIO
                payment.setSubtotal(new BigDecimal("1.50"));
                //PRENDO DA DB
                payment.setCurrencyType("EUR");
                //PRENDO DA DB
                payment.setRecipient("diegofun89-facilitator@hotmail.it");

                payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

                Intent checkoutIntent = PayPal.getInstance().checkout(payment, PayPalTestActivity.this);

                startActivityForResult(checkoutIntent, 1);
            }

        });
        Button contact = new Button(this);
        contact.setText(R.string.send_email);
        params.setMargins(50,100,50,100);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"mail dal server"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.object));
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.final_message));
                startActivity(Intent.createChooser(emailIntent, "Invia email..."));;
            }
        });
        contact.setLayoutParams(params);
        layoutSimplePayment.addView(contact);
        layoutSimplePayment.addView(launchSimplePayment);
        layoutSimplePayment.setGravity(Gravity.CENTER);
        setContentView(layoutSimplePayment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        switch (resultCode) {
            case Activity.RESULT_OK:
                //Il pagamento è stato effettuato
                String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                CharSequence text = "OK " + payKey;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            case Activity.RESULT_CANCELED:
                text = "CANCELED";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                // Il pagamento è stato cancellato dall’utente
                break;
            case PayPalActivity.RESULT_FAILURE:
                // Il pagamento non è stato effettuato a causa di errore
                String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);

                text = "ERROR " + errorID + errorMessage;
                toast = Toast.makeText(context, text, duration);
                toast.show();

        }
    }
}
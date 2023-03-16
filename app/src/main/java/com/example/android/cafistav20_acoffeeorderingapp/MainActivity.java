package com.example.android.cafistav20_acoffeeorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import javax.security.auth.Subject;


public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the Plus button is clicked.
     */
    public void increment(View view){
        //So that the user cannot order more than 100 coffees.
        if(quantity == 100){
            //Show an error message as Toast.
            Toast.makeText(this , "You cannot have more than 100 coffees" , Toast.LENGTH_SHORT).show();

            //Exit the method early because there is nothing to do.
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the Minus button is clicked.
     */
    public void decrement(View view){
        //So that user cannot order coffees in negative number.
        if(quantity == 1){
            //Show an error message as Toast.
            Toast.makeText(this , "You cannot have less than 1 coffee" , Toast.LENGTH_SHORT).show();

            //Exit the method early because there is nothing to do.
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the Order button is clicked.
     */
    public void submitOrder(View view) {
        //Getting the name of the user.
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        //Figure out if the user wants whipped cream topping.
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
       // Log.v("MainActivity", "Has Whipped Cream: " + hasWhippedCream);  //this will only appear in Logcat. It is used just to check whether the right value is getting printed or not. we are here using it simply for debugging.

        //Figure out if the user wants chocolate topping.
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price , hasWhippedCream , hasChocolate);


        //When user clicks on Submit button, order summary is passed to email app via intents.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        //SUBJECT of email app should be filled with this:
        intent.putExtra(Intent.EXTRA_SUBJECT, "Cafista order for " + name);

        //BODY of email app should be filled with this:
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);

        //If there is no email app in user's phone, then the app can crash. resolveActivity makes sure this
        // ...DOESN'T happen.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * This method calculates the price of the order.
     *
     * @param addWhippedCream is whether or not user wants the whipped cream topping.
     * @param addChocolate is whether or not user wants the chocolate topping.
     * @return total price.
     */
    private int calculatePrice(boolean addWhippedCream , boolean addChocolate){
        //Price of one cup of coffee.
        int basePrice = 5;

        //add $1 if the user wants whipped cream.
        if(addWhippedCream){
            basePrice = basePrice + 1;
        }

        //add $2 if the user wants chocolate.
        if(addChocolate){
            basePrice = basePrice + 2;
        }

        return quantity * basePrice;
    }

    /**
     * create the summary of the order.
     *
     * @param name of the customer.
     * @param price of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping to the coffee.
     * @param addChocolate is whether or not the user wants chocolate topping.
     * @return text summary.
     */
    private String createOrderSummary(String name , int price, boolean addWhippedCream , boolean addChocolate){
        String priceMessage = getString(R.string.order_summary_name, name);
        //The above line is defined in strings.xml file. it can also be written as:
        //String priceMessage = "Name: " + name;    - This is hardcoded here

        priceMessage += "\nAdd whipped cream? " + addWhippedCream;
        priceMessage += "\nAdd chocolate? " + addChocolate;
        priceMessage = priceMessage + "\nQuantity: " + quantity;
        priceMessage = priceMessage + "\nTotal: $" + price;

        priceMessage = priceMessage + "\n" + getString(R.string.thank_you);
        //The 'Thank you' in above line is NOT hardcoded. It's defined in strings.xml. NOT hardcoding
        //... the string will allow us to have multiple translation of stings in different languages.
        //You can also write the above line as shown below(we've hardcoded the 'Thank you' string) :
        //priceMessage = priceMessage + "\nThank you!"; //or, priceMessage += "\nThank you!";
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityValueTv = findViewById(R.id.quantity_value_tv);
        quantityValueTv.setText("" + numberOfCoffees); //an empty string is concatenated here
        //because setText cannot take an integer, however, concatening a string with int works.
    }



}

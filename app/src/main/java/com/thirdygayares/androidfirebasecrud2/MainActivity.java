package com.thirdygayares.androidfirebasecrud2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextId, editTextName, editTextCategory, editTextPrice;
    TextView textViewResults;
    Button buttonAddProduct, buttonGetProduct;

    List<Product> productList = new ArrayList<>();
    Product productObject;
    StringBuilder stringBuilder;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        stringBuilder = new StringBuilder();
        productObject = new Product();
        firestore = FirebaseFirestore.getInstance();


        editTextId= findViewById(R.id.editTextId);
        editTextName= findViewById(R.id.editTextName);
        editTextCategory= findViewById(R.id.editTextCategory);
        editTextPrice= findViewById(R.id.editTextPrice);
        textViewResults= findViewById(R.id.textViewResults);
        buttonAddProduct= findViewById(R.id.buttonAddProduct);
        buttonGetProduct= findViewById(R.id.buttonGetProducts);


        //listener for buttonAddProduct
        buttonAddProduct.setOnClickListener(v-> addFunction());

        //btnGetProduct listener
        buttonGetProduct.setOnClickListener(v-> getFunction());

    }


    //TODO add FUNCTION
    private void addFunction(){

        productObject.setId(Integer.parseInt(editTextId.getText().toString()));
        productObject.setName(editTextName.getText().toString());
        productObject.setCategory(editTextCategory.getText().toString());
        productObject.setPrice(Integer.parseInt(editTextPrice.getText().toString()));

        //TESTING
        Log.d("MAIN",  "id" + productObject.getId() + " name: " + productObject.getName() + " category: " + productObject.getCategory() +  "PRICE:" + productObject.getPrice()) ;

        firestore.collection("product")
                        .add(productObject).addOnSuccessListener(documentReference -> {
                        stringBuilder.append("\n\n Name:" + productObject.getName());
                        stringBuilder.append("\n id:" + productObject.getId());
                        stringBuilder.append("\n category:" + productObject.getCategory());
                        stringBuilder.append("\n price:" + productObject.getPrice());


                        textViewResults.setText(stringBuilder.toString());
                }).addOnFailureListener(e-> {
                    Log.e("MAIN", e.getMessage());
                });
    }

    private void getFunction(){
        StringBuilder stringBuilder2 = new StringBuilder();

        firestore.collection("product")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()){

                        for(QueryDocumentSnapshot document: task.getResult()){
                            Product product = document.toObject(Product.class);
                            stringBuilder2.append("\n\n Name:" + product.getName());
                            stringBuilder2.append("\n id:" + product.getId());
                            stringBuilder2.append("\n category:" + product.getCategory());
                            stringBuilder2.append("\n price:" + product.getPrice());
                        }
                        textViewResults.setText(stringBuilder2.toString());
                    }
                    else{
                        Log.e("MAIN", task.getException().getMessage());
                    }
                }).addOnFailureListener(e->{
                   Log.e("MAIN", e.getMessage());
                });
    }

}
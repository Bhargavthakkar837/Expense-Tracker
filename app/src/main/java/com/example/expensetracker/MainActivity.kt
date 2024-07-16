package com.example.expensetracker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.expensetracker.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var expensesLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize spinners
        setupSpinners()

        // Initialize expenses layout
        expensesLayout = findViewById(R.id.expensesLayout)

        // Set onClickListener for Add Expense button
        binding.addButton.setOnClickListener {
            onAddExpenseClicked()
        }
    }

    private fun setupSpinners() {
        val categories = resources.getStringArray(R.array.categories)
        val accountTypes = resources.getStringArray(R.array.account_types)

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoryAdapter

        val accountTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypes)
        accountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.accountTypeSpinner.adapter = accountTypeAdapter
    }

    private fun onAddExpenseClicked() {
        val expenseName = binding.expenseNameEditText.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()
        val accountType = binding.accountTypeSpinner.selectedItem.toString()
        val amount = binding.amountEditText.text.toString().toDoubleOrNull()

        if (amount == null) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new expense view
        val expenseView = createExpenseView(expenseName, category, accountType, amount)

        // Add expense view to expenses layout
        expensesLayout.addView(expenseView)

        // Scroll to the bottom of the ScrollView
        binding.expensesScrollView.post {
            binding.expensesScrollView.fullScroll(View.FOCUS_DOWN)
        }

        // Clear fields
        binding.expenseNameEditText.text.clear()
        binding.amountEditText.text.clear()
    }

    @SuppressLint("SetTextI18n")
    private fun createExpenseView(expenseName: String, category: String, accountType: String, amount: Double): View {
        // Inflate expense_item.xml layout
        val inflater = LayoutInflater.from(this)
        val expenseView = inflater.inflate(R.layout.item_expense, expensesLayout, false)
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        // Set text views
        expenseView.findViewById<TextView>(R.id.expenseNameTextView).text = "Expense Name: $expenseName"
        expenseView.findViewById<TextView>(R.id.categoryTextView).text = "Category: $category"
        expenseView.findViewById<TextView>(R.id.accountTypeTextView).text = "Account Type: $accountType"
        expenseView.findViewById<TextView>(R.id.amountTextView).text = "Amount: $amount"
        expenseView.findViewById<TextView>(R.id.dateTextView).text = "Date: $currentDate"

        // Add onClickListener to remove this expense
        expenseView.setOnClickListener {
            expensesLayout.removeView(expenseView)
        }

        return expenseView
    }

    private fun getCurrentDate(): String {
        return android.text.format.DateFormat.format("dd-MM-yyyy", java.util.Date()).toString()
    }
}

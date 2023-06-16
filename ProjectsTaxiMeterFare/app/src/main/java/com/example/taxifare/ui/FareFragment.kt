package com.example.taxifare.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taxifare.R
import com.example.taxifare.model.Taxi
import kotlinx.android.synthetic.main.fragment_fare.*

class FareFragment(private val taxi: Taxi) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hiển thị thông tin giá cước và xe taxi
        fare_text_view.text = getString(R.string.fare_text, taxi.fare)
        taxi_text_view.text = getString(R.string.taxi_text, taxi.company, taxi.model)
    }
}
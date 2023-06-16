package com.example.taxifare.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taxifare.R
import com.example.taxifare.adapter.HistoryAdapter
import com.example.taxifare.db.DatabaseHandler
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập adapter và layoutManager cho recyclerView
        historyAdapter =```kotlin
        HistoryAdapter()
        history_recycler_view.adapter = historyAdapter
        history_recycler_view.layoutManager = LinearLayoutManager(requireContext())

        // Lấy dữ liệu từ cơ sở dữ liệu và đưa vào adapter
        databaseHandler = DatabaseHandler(requireContext())
        val historyList = databaseHandler.getAllHistory()
        historyAdapter.setHistoryList(historyList)
    }
}
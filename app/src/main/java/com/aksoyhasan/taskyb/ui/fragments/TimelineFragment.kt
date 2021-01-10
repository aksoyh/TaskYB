package com.aksoyhasan.taskyb.ui.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksoyhasan.taskyb.R
import com.aksoyhasan.taskyb.adapter.FeaturedAdapter
import com.aksoyhasan.taskyb.adapter.MentionsAdapter
import com.aksoyhasan.taskyb.adapter.MentionsImageAdapter
import com.aksoyhasan.taskyb.adapter.TimelineAdapter
import com.aksoyhasan.taskyb.ui.MainActivity
import com.aksoyhasan.taskyb.ui.MainViewModel
import com.aksoyhasan.taskyb.util.Constants.Companion.QUERY_PAGE_SIZE
import com.aksoyhasan.taskyb.util.Resource

class TimelineFragment: Fragment(R.layout.fragment_timeline) {

    private lateinit var toolbarTitle: TextView
    private lateinit var rvFeatured: RecyclerView
    private lateinit var rvTimeline: RecyclerView
    private lateinit var layoutMentionsList: RelativeLayout
    private lateinit var btnMentionsClose: Button

    private lateinit var featuredAdapter: FeaturedAdapter
    private lateinit var mentionsAdapter: MentionsAdapter
    private lateinit var mentionsImageAdapter: MentionsImageAdapter
    private lateinit var timelineAdapter: TimelineAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    lateinit var viewModel: MainViewModel

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        navController = Navigation.findNavController(requireActivity(), R.id.ac_ma_nav_host_fragment)

        initViews(view)
        setToolbarText()
        setupRecyclerViews()
        setupButtons()
    }

    private fun setupButtons() {
        mentionsImageAdapter.setOnItemClickListener {
            layoutMentionsList.visibility = View.VISIBLE
        }
        btnMentionsClose.setOnClickListener {
            layoutMentionsList.visibility = View.GONE
        }
    }

    private fun initViews(view: View) {
        toolbarTitle = view.findViewById(R.id.fr_tl_toolbar_tv_header)
        rvFeatured = view.findViewById(R.id.fr_tl_featured_rv)
        rvTimeline = view.findViewById(R.id.fr_tl_timeline_rv)

        layoutMentionsList = view.findViewById(R.id.fr_mentions_list)
        btnMentionsClose = view.findViewById(R.id.layout_mentions_btn_close)

    }

    private fun setToolbarText() {
        val firstText = "DEMO"
        val secondText = " FEED"

        val s = SpannableStringBuilder()
            .append(firstText)
            .bold { append(secondText) }
        toolbarTitle.text = s
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getRecordById()
                isScrolling = false
            } else {
                rvTimeline.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
                (activity as MainActivity).hideBottomNavigationView()
            } else {
                (activity as MainActivity).showBottomNavigationView()
            }
        }
    }

    private fun setupRecyclerViews() {
        featuredAdapter = FeaturedAdapter()
        mentionsAdapter = MentionsAdapter()
        mentionsImageAdapter = MentionsImageAdapter()
        timelineAdapter = TimelineAdapter()

        rvFeatured.apply {
            adapter = featuredAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            viewModel.getAllCitiesWithInfo()
        }
        rvTimeline.apply {
            adapter = timelineAdapter
            layoutManager = LinearLayoutManager(activity)
            viewModel.getRecordById()
            //addOnScrollListener(this@TimelineFragment.scrollListener)
        }

        viewModel.getAllCitiesWithInfoRes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.data?.let { getAllCitiesWithInfoResponse ->
                        if (getAllCitiesWithInfoResponse.featured != null) {
                            featuredAdapter.differ.submitList(getAllCitiesWithInfoResponse.featured.toList())
                        } else {
                            Toast.makeText(requireContext(), "Liste bulunamadı", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Error -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    (requireActivity() as MainActivity).showLoading()
                }
            }
            response.data = null
            response.message = null
        })

        viewModel.getRecordByIdRes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.data?.let { getRecordByIdResResponse ->
                        if (getRecordByIdResResponse.timeline != null) {
                            timelineAdapter.differ.submitList(getRecordByIdResResponse.timeline.toList())
                        } else {
                            Toast.makeText(requireContext(), "Liste bulunamadı", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Error -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    (requireActivity() as MainActivity).showLoading()
                }
            }
            response.data = null
            response.message = null
        })
    }
}
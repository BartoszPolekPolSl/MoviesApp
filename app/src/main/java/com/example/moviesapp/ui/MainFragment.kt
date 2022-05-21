package com.example.moviesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.MovieCardClickListener
import com.example.moviesapp.databinding.FragmentMainBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.util.SwipeGesture
import com.example.moviesapp.viewmodel.MoviesAdapter
import com.example.moviesapp.viewmodel.MoviesViewModel

class MainFragment : Fragment(), MovieCardClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val moviesAdapter = MoviesAdapter()

    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRealtimeUpdates()
        binding.fabAddMovie.setOnClickListener{
            AddMovieDialogFragment()
                .show(childFragmentManager, "")
        }

        moviesAdapter.listener=this
        binding.moviesRecyclerView.let {
            it.adapter = moviesAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            setGesture(it)
        }
        viewModel.moviesList.observe(viewLifecycleOwner){
            moviesAdapter.filterList(it)
        }
        viewModel.watchedFilterStatus.observe(viewLifecycleOwner){
            moviesAdapter.watchedStatus = it
            moviesAdapter.filterList(viewModel.moviesList.value)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.changeFilterStatus()
        return true
    }

    private fun setGesture(recyclerView: RecyclerView){
        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        viewModel.deleteMovie(moviesAdapter.getMovieById(viewHolder.bindingAdapterPosition))
                    }
                    ItemTouchHelper.RIGHT ->{
                        viewModel.changeWatched(moviesAdapter.getMovieById(viewHolder.bindingAdapterPosition))
                    }
                }
            }

        }

        ItemTouchHelper(swipeGesture).attachToRecyclerView(recyclerView)
    }

    override fun onMovieCardClick(movie: Movie) {
        Log.d("lol","lol")
        AddMovieDialogFragment(movie).show(childFragmentManager, "")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
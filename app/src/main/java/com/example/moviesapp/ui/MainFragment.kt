package com.example.moviesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.MovieCardClickListener
import com.example.moviesapp.databinding.DialogFragmentAddMovieBinding
import com.example.moviesapp.databinding.FragmentMainBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewmodel.MoviesAdapter
import com.example.moviesapp.viewmodel.MoviesViewModel

class MainFragment : Fragment(), MovieCardClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val adapter = MoviesAdapter()
        adapter.listener=this
        binding.moviesRecyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.moviesList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
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
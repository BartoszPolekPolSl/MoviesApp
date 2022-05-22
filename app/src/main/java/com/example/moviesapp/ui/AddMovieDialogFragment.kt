package com.example.moviesapp.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.moviesapp.databinding.DialogFragmentAddMovieBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.WatchedOptions
import com.example.moviesapp.model.ratingOptionList
import com.example.moviesapp.viewmodel.MoviesViewModel


class AddMovieDialogFragment(private val movie: Movie? = null) : DialogFragment() {

    private var _binding: DialogFragmentAddMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFragmentAddMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val watchedAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            WatchedOptions.values()
        )
        val ratingAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            ratingOptionList
        )
        binding.watchedComplete.setAdapter(watchedAdapter)
        binding.ratingCompleteText.setAdapter(ratingAdapter)
        binding.saveButton.setOnClickListener {
            addMovie()
        }
        bindDialog()
    }

    private fun addMovie() {
        if(isInputCorrect()){
            val title = binding.titleEditTxt.text.toString()
            val director = binding.directorEditTxt.text.toString()
            val year = binding.yearEditTxt.text.toString()
            val rating = binding.ratingCompleteText.text.toString()
            val watched = binding.watchedComplete.text.toString() == WatchedOptions.Yes.toString()

            val movie = Movie(
                id= this.movie?.id,
                rating = rating.toIntOrNull(),
                director = director,
                year = year.toIntOrNull(),
                watched = watched,
                title = title
            )
            if(this.movie!=null){
                viewModel.updateMovie(movie)
            }
            else{
                viewModel.addMovie(movie)
            }
            dismiss()
        }

    }

    private fun isInputCorrect(): Boolean{
        if(binding.titleEditTxt.text.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Please enter movie title.", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun bindDialog() {
        if (movie != null) {
            binding.watchedComplete.setText(if(movie.watched==WatchedOptions.Yes.bool){WatchedOptions.Yes.toString()}else{WatchedOptions.No.toString()}, false)
            binding.directorEditTxt.setText(movie.director)
            binding.titleEditTxt.setText(movie.title)
            binding.yearEditTxt.setText(movie.year.toString())
            binding.ratingCompleteText.setText(movie.rating.toString(), false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.moviesapp.util

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewmodel.MoviesAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context : Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ){

    private val deleteColor = ContextCompat.getColor(context, R.color.deleteColor )
    private val deleteIcon = R.drawable.ic_baseline_delete_24
    private val changeWatchedColor = ContextCompat.getColor(context, R.color.changeWatchedColor )
    private val unwatchedIcon = R.drawable.ic_baseline_visibility_off_24
    private val watchedIcon = R.drawable.ic_baseline_visibility_24


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val holder = viewHolder as MoviesAdapter.MovieViewHolder
        val adapter = recyclerView.adapter as MoviesAdapter
        val recyclerPosition = holder.bindingAdapterPosition
        if(recyclerPosition==-1||adapter.itemCount==0){
            return
        }
        else{
            val movie = adapter.getMovieByRecyclerPosition(holder.bindingAdapterPosition)
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addSwipeLeftBackgroundColor(deleteColor)
                .addSwipeLeftActionIcon(deleteIcon)
                .addSwipeRightBackgroundColor(changeWatchedColor)
                .addSwipeRightActionIcon(if(movie.watched){unwatchedIcon}else{watchedIcon})
                .create()
                .decorate()

        }

    }

}
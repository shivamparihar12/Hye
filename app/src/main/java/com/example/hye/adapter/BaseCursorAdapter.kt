package com.example.hye.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

abstract class BaseCursorAdapter<V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {


    private var mCursor: Cursor? = null
    private var mDataValid: Boolean? = false
    private var mRowIdColumn: Int? = null


    abstract fun onBindViewHolder(holder: V, cursor: Cursor)

    override fun onBindViewHolder(holder: V, position: Int) {
        if (!mDataValid!!) {
            throw IllegalStateException("Cannot bind View Holder when cursor is in invalid state.")
        }
        if (!mCursor!!.moveToPosition(position)) {
            throw IllegalStateException("Could not move cursor to position" + position + "when trying to bind view holder")
        }
        mCursor?.let { onBindViewHolder(holder, it) }
    }

    override fun getItemCount(): Int {
        if (mDataValid!!) {
            return mCursor!!.count
        } else return 0
    }


    override fun getItemId(position: Int): Long {
        if (!mDataValid!!) {
            throw IllegalStateException("Cannot lookup item id when cursor is in invalid state,")
        }
        if (!mCursor!!.moveToPosition(position)) {
            throw IllegalStateException("Could not move cursor to position" + position + "when trying to get an item id")
        }
        return mCursor!!.getLong(mRowIdColumn!!)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swapCursor(newCursor: Cursor?) {
        if (newCursor == mCursor) {
            return
        }
        if (newCursor != mCursor) {
            mCursor = newCursor!!
            mDataValid = true
            notifyDataSetChanged()
        }
        //        else {
//            notifyItemRangeRemoved(0, itemCount)
//            mCursor = null
//            mRowIdColumn = -1
//            mDataValid = false
//        }
    }

}
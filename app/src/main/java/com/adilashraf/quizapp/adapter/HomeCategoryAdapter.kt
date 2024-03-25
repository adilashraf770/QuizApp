package com.adilashraf.quizapp.adapter

 import android.content.Context
 import android.content.Intent
 import android.view.LayoutInflater
 import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adilashraf.quizapp.QuizActivity
import com.adilashraf.quizapp.databinding.HomeCategoryLayoutBinding
import com.adilashraf.quizapp.model.HomeCategory

  class HomeCategoryAdapter(val categoryList: ArrayList<HomeCategory>, val context: Context)
      : RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder>() {

    class HomeCategoryViewHolder(val binding: HomeCategoryLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder =
    HomeCategoryViewHolder(HomeCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
         val pos = categoryList[position]
        holder.binding.categoryTitle.text = pos.title
        holder.binding.categoryImg.setImageResource(pos.img)

        holder.binding.categoryCard.setOnClickListener{
            val i = Intent(context, QuizActivity::class.java)
            i.putExtra("categoryImg", pos.img)
            i.putExtra("categoryTitle", pos.title)

            context.startActivity(i)

        }
    }
}
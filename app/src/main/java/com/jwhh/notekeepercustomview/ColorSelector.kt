package com.jwhh.notekeepercustomview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jwhh.notekeepercustomview.databinding.ColorSelectorBinding
import kotlinx.android.synthetic.main.color_selector.view.*

class ColorSelector @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null,
                                              defStyle: Int = 0, defRes: Int=0 ): LinearLayout(context,attributeSet,defStyle, defRes) {
    private lateinit var binding:ColorSelectorBinding
    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0
    var selectedColorValue:Int = android.R.color.transparent
        set(value) {
            var index = listOfColors.indexOf(value)
            if(index == -1){
                colorEnabled.isChecked = false
                index=0
            }else{
                colorEnabled.isChecked = true
            }
            selectedColorIndex = index
            selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        }

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.ColorSelector)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors)
            .map {
                Color.parseColor(it.toString())
            }

            orientation = LinearLayout.HORIZONTAL
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            inflater.inflate(R.layout.color_selector, this)
            binding = ColorSelectorBinding.inflate(inflater, this)

            binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

            binding.colorSelectorArrowLeft.setOnClickListener{
                selectPreviousColor()
            }
            binding.colorSelectorArrowRight.setOnClickListener {
                selectNextColor()
            }
            binding.colorEnabled.setOnCheckedChangeListener { button, isChecked ->
                broadcastColor()
            }

        }

    private fun selectNextColor() {
        if(selectedColorIndex == listOfColors.lastIndex){
            selectedColorIndex = 0
        }else{
            selectedColorIndex++
        }
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

    }

    private fun selectPreviousColor() {
        if(selectedColorIndex == 0){
            selectedColorIndex = listOfColors.lastIndex
        }else{
            selectedColorIndex--
        }
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

    }
//Creating a listener instead of an inteface. Using a high order function
    private var colorSelectListeners: ArrayList<(Int) -> Unit> = arrayListOf()
    fun addListener(function: (Int)-> Unit){
        this.colorSelectListeners.add(function)
    }


    private fun broadcastColor(){
        val color = if(binding.colorEnabled.isChecked)
            listOfColors[selectedColorIndex]
        else
            Color.TRANSPARENT
        this.colorSelectListeners.forEach { function ->
            function(color)
        }
    }



}
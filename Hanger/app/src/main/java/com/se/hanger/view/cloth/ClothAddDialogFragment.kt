package com.se.hanger.view.cloth

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.se.hanger.R
import com.se.hanger.data.db.ClothDatabase
import com.se.hanger.data.model.*
import com.se.hanger.databinding.FragmentDialogClothAddBinding
import com.se.hanger.utils.ScreenSizeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClothAddDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentDialogClothAddBinding
    private lateinit var clothDB: ClothDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogClothAddBinding.inflate(inflater)
        clothDB = ClothDatabase.getInstance(requireContext())!!
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        // full Screen code
        val width = (ScreenSizeProvider.getWidth(
            requireContext(),
            requireActivity().windowManager
        ))
        val height = (ScreenSizeProvider.getHeight(
            requireContext(),
            requireActivity().windowManager
        ))
        dialog?.window?.setLayout((width * 0.9).toInt(), (height * 0.9).toInt())
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun setClickListener() {
        with(binding) {
            /* 추가 버튼 이벤트 설정*/
            clothAddBtn.setOnClickListener {
                // DB 는 IO 작업이기 때문에 scope 열어줌
                // TODO 각종 ui 에서 데이터 가져와서 설정해주자
                CoroutineScope(Dispatchers.IO).launch {
                    val cloth = Cloth(
                        buyUrl = buyerEt.text.toString(),
                        clothSize = "",
                        clothName = "",
                        clothMemo = "",
                        clothPhoto = "",
                        dailyPhoto = listOf(Photo("", "")),
                        tags = listOf(Tag("", "")),
                        categories = listOf(Category(Season.SPRING, CategoryCloth.ACCESSORY))
                    )
                    clothDB.clothDao().insert(cloth)
                }
            }

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.close_btn -> {
                dismiss()
            }
        }
    }
}
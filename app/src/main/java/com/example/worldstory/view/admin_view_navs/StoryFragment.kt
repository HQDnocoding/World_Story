package com.example.worldstory.view.admin_view_navs

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStoryBinding
import com.example.worldstory.dat.admin_adapter.OnItemClickListener
import com.example.worldstory.dat.admin_adapter.StoryAdapter
import com.example.worldstory.view.admin_dialog.AddStoryDialog
import com.example.worldstory.view.admin_sheet.MyBottomSheetFragment
import com.example.worldstory.view.admin_view_navs.chapter_activity.ChapterActivity
import com.example.worldstory.view_models.admin_viewmodels.RecyclerViewState
import com.example.worldstory.view_models.admin_viewmodels.SharedViewModel
import com.example.worldstory.view_models.admin_viewmodels.StoryViewModel
import com.example.worldstory.view_models.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.isUserCurrentAdmin
import com.example.worldstory.data.model.Story
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StoryFragment : Fragment(), OnItemClickListener {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var storyAdapter: StoryAdapter
    private var isSearchViewOpen = false

        private val storyViewModel: StoryViewModel by activityViewModels {
            StoryViewModelFactory(DatabaseHelper(requireActivity()), 0)
        }

    private lateinit var binding: FragmentStoryBinding

    private var type = -1

    private val DEBOUNCE_DELAY = 500L
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //spinner
        val spinner: Spinner = binding.menuStory

        ArrayAdapter.createFromResource(
            requireContext(), R.array.story_options, android.R.layout.simple_expandable_list_item_1
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        savedInstanceState?.let {
            val selectedPosition = it.getInt("type")
            spinner.setSelection(selectedPosition)
        }

        spinner.setSelection(storyViewModel.type)


        spinner.onItemSelectedListener = object : OnItemSelectedListener, OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (type != position) {
                    type = position
                    storyViewModel.fetchAllStoriesByType(type)
                }
            }

            override fun onItemClick(item: Story) {
                if (spinner.isEnabled == false) {
                    Toast.makeText(
                        requireContext(), "Bỏ lọc thể loại để đổi loại truyện", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddEvent ->
            if (isAddEvent == true) {
                onAddButtonClicked()  // Gọi hàm xử lý khi sự kiện xảy ra
                sharedViewModel.addHandled()
            }
        }

        //tạo list
        binding.storyList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //thêm item
        val color1 = ContextCompat.getColor(requireContext(), R.color.sweetheart)
        storyAdapter = StoryAdapter(
            storyViewModel.stories.value?.toMutableList() ?: mutableListOf(), color1, this
        )
        binding.storyList.adapter = storyAdapter
        storyViewModel.stories.observe(viewLifecycleOwner) {
            storyAdapter.updateList(storyViewModel.stories.value ?: emptyList())
        }


        //searchview
        binding.searchViewStory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                runnable?.let { handler.removeCallbacks(it) }

                runnable = Runnable {
                    p0?.let {
                        storyAdapter.filter.filter(it)
                    }
                }

                handler.postDelayed(runnable!!, DEBOUNCE_DELAY)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                storyAdapter.filter.filter(p0)
                sharedViewModel.searchQueryStory.value = p0
                return false
            }
        })
        sharedViewModel._search.observe(viewLifecycleOwner) { isSearchClicked ->
            if (isSearchClicked == true) {
                showSearchView()
                sharedViewModel.searchHandle()
            }
        }
        binding.searchViewStory.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
                sharedViewModel
            }

        sharedViewModel.searchQueryStory.observe(viewLifecycleOwner) { query ->
            if (sharedViewModel.searchQueryStory.value != "") {
                binding.searchViewStory.visibility = View.VISIBLE
                binding.searchViewStory.isIconified = false
                binding.searchViewStory.setQuery(query, false)
            }
        }
        sharedViewModel.recyclerViewStateStory.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(it.firstVisibleItemPosition, it.offset)
            }
        })

        //Bottom sheet
        val showBottomSheetButton =
            view.findViewById<FloatingActionButton>(R.id.floating_action_button)

        showBottomSheetButton.setOnClickListener {
            val bottomSheet = MyBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "MyBottomSheet")
        }

        ////Filter Buttun

        sharedViewModel._filterBtn.observe(viewLifecycleOwner) { isClicked ->
            storyAdapter.filterByCates(sharedViewModel._selectedChips, storyViewModel)
            if (sharedViewModel._selectedChips.isEmpty()) {
                binding.menuStory.isEnabled = true
            } else binding.menuStory.isEnabled = false
        }


//swipe
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val story = storyAdapter.getStory(position)
                if (direction == ItemTouchHelper.LEFT) {

                    if (requireContext().isUserCurrentAdmin() || requireContext().getUserIdSession() == story.userID) {
                        showConfirmationDialog(story, position)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Bạn không có quyền xóa đối tượng này",
                            Toast.LENGTH_SHORT

                        ).show()
                        storyAdapter.notifyItemChanged(position)
                    }


                } else if (direction == ItemTouchHelper.RIGHT) {

                    if (requireContext().isUserCurrentAdmin() || requireContext().getUserIdSession() == story.userID) {
                        showConfirmationDialog(story, position)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Bạn không có quyền xóa đối tượng này",
                            Toast.LENGTH_SHORT

                        ).show()
                        storyAdapter.notifyItemChanged(position)
                    }
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.7f
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val context = recyclerView.context

                // Lấy icon và kích thước
                val icon = ContextCompat.getDrawable(context, R.drawable.white_outline_delete_24)!!
                val iconIntrinsicHeight = icon.intrinsicHeight

                // Tính toán vị trí icon
                val iconMargin = (itemView.height - iconIntrinsicHeight) / 2

                // Thêm nền khi vuốt
                val paint = Paint()

                if (dX < 0) { // Vuốt
                    paint.color = Color.RED
                    canvas.drawRect(
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        paint
                    )
                    val iconTop = itemView.top + iconMargin
                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = iconTop + icon.intrinsicHeight

                    // Vẽ icon lên Canvas
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon.draw(canvas)
                } else {
                    paint.color = Color.RED
                    canvas.drawRect(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.left + dX,
                        itemView.bottom.toFloat(),
                        paint
                    )
                    val iconTop = itemView.top + iconMargin
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = iconLeft + icon.intrinsicWidth
                    val iconBottom = iconTop + icon.intrinsicHeight

                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon.draw(canvas)

                }
                super.onChildDraw(
                    canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.storyList)
    }

    private fun onAddButtonClicked() {

        AddStoryDialog().show(parentFragmentManager, "AddStoryDialogFragment")
    }


    override fun onResume() {
        super.onResume()
        sharedViewModel.onFilterBtnClicked()
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.onFilterBtnClicked()
    }


    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
        val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val offset = binding.storyList.getChildAt(0)?.top ?: 0

        // Cập nhật ViewModel với trạng thái cuộn
        sharedViewModel.recyclerViewStateStory.value =
            RecyclerViewState(firstVisibleItemPosition, offset)
    }

    override fun onItemClick(item: Story) {
        if (requireContext().isUserCurrentAdmin() || requireContext().getUserIdSession() == item.userID) {
            val intent = Intent(requireContext(), ChapterActivity::class.java)
            intent.putExtra("ID", item.storyID)
            intent.putExtra("type", storyViewModel.type)
            startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(), "Bạn không có quyền truy cập truyện này", Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::binding.isInitialized) {
            outState.putInt("type", binding.menuStory.selectedItemPosition)
        }
    }

    private fun showSearchView() {
        binding.searchViewStory.visibility = View.VISIBLE
        binding.searchViewStory.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
        binding.searchViewStory.requestFocus()
    }

    private fun hideSearchView() {
        binding.searchViewStory.setQuery("", false)  // Xóa nội dung tìm kiếm
        binding.storyList.clearFocus()
        binding.searchViewStory.visibility = View.GONE
        sharedViewModel.searchQueryStory.value = ""
    }

    private fun showConfirmationDialog(story: Story, p: Int) {
        // Tạo AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Có chắc muốn xóa không?").setCancelable(false)
            .setPositiveButton("Chấp nhận") { dialog, id ->
                storyViewModel.deleteStory(story)
                dialog.dismiss()
            }.setNegativeButton("Không chấp nhận") { dialog, id ->
                storyAdapter.notifyItemChanged(p)
                dialog.dismiss()
            }


        val dialog = builder.create()
        dialog.show()
    }
}
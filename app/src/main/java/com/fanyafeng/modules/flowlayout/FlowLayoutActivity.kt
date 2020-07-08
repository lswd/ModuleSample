package com.fanyafeng.modules.flowlayout

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fanyafeng.modules.BaseActivity
import com.fanyafeng.modules.R
import com.ripple.log.LogFactory
import com.ripple.tool.density.dp2px
import com.ripple.tool.extend.forEachAnchor
import com.ripple.tool.extend.forEach
import com.ripple.tool.int.oneQuadra
import com.ripple.tool.screen.getScreenWidth
import com.ripple.tool.string.isNotNullOrEmpty
import com.ripple.ui.flowview.IChooseModel
import com.ripple.ui.flowview.impl.ChooseFlowView
import com.ripple.ui.flowview.impl.ChooseItemView
import kotlinx.android.synthetic.main.activity_flow_layout.*
import java.util.*

class FlowLayoutActivity : BaseActivity() {

    private val TAG = FlowLayoutActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "自适应Layout"
        setContentView(R.layout.activity_flow_layout)

        initView()
        initData()
    }

    var list = mutableListOf<IChooseModel>()

    private fun initView() {


        5.forEach {
            val model = ChooseModel("我是第$it", it != 3, it == 0)
            list.add(model)
            val itemView = ChooseItemView(this)
            val tagView = itemView.getTagView()
            itemView.setInnerTagWrapContent()
            itemView.chooseViewUnselected = R.drawable.choose_view_normal
            chooseItemView.addItemView(itemView, model)
        }

        chooseItemView.onItemClickListener = { first, position, third, fourth, fifth ->
            //不论按钮状态，只要点击就会有回调
            Log.d(TAG, "被点击：" + position)
        }

        chooseItemView.onItemAbleClickListener = { view, position, model ->
            Log.d(TAG, "被点击：" + position)
            showToast("被点击：" + position)

        }

        chooseItemView.onItemUnableClickListener = { view, position, model ->
            Log.d(TAG, "不能被点击的被点击了：" + position)
            showToast("不能被点击的被点击了：" + position)
        }

        val itemWidth = (getScreenWidth() - 10.dp2px).oneQuadra
        val layoutParams =
            LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
//        layoutParams.leftMargin = 5.dp2px
//        layoutParams.rightMargin = 5.dp2px
        splitFlowView.setMinChooseCount(1)
        4.forEachAnchor { it, isFirst, isLast ->
//            println("我是得几个：" + it)
//            println("是否是第一个：" + isFirst.toString())
//            println("是否是最后一个：" + isLast.toString())
            val model = ChooseModel("我是第$it", it != 3, it == 0)
            list.add(model)
            val itemView = ChooseItemView(this)
            itemView.layoutParams = layoutParams
            splitFlowView.addItemView(itemView, model)
        }


        splitFlowView.onItemClickListener =
            { first: View?, position: Int?, third: IChooseModel?, fourth: Boolean?, fifth: Boolean? ->
                println("被点击位置：" + position)
                showToast("被点击位置：" + position)
            }

        javaAddView(this)
    }

    private fun initData() {
        val viewList = mutableListOf<Pair<IChooseModel, ChooseItemView>>()
        (0 until 7).forEach { it ->
            val title = if (it < 5) {
                "我是第$it 被更新"
            } else {
                "我是新增的第$it 个view"
            }
            val model = ChooseModel(title, true, (it == 3 || it == 4))
            val itemView = ChooseItemView(this)
            if (it < 4) {
                itemView.setInnerTagWrapContent()
            }
            viewList.add(Pair(model, itemView))
        }
        btn1.setOnClickListener {
            chooseItemView.updateView(viewList)
        }

        btn2.setOnClickListener {
            Log.d(TAG, "最后结果：" + chooseItemView.getSelectedResult().toString())
            Log.d(TAG, "所有列表状态：" + chooseItemView.getAllDataList().toString())
            showToast("最后结果：" + chooseItemView.getSelectedResult().toString())
        }

    }

    /**
     * 所有商品集合
     */
    private val shopList =
        listOf(
            "a1-b1-c1",
            "a2-b1-c1",
            "a3-b2-c2",
            "a1-b2-c1",
            "a1-b2-c3",
            "a1-b3-c1",
            "a1-b4-c1",
            "a1-b1-c2",
            "a1-b1-c3"
        )

    /**
     * 所有规格集合
     */
    private val aSpecList = mutableListOf(
        ChooseModel(id = "a1", name = "红色", checked = true),
        ChooseModel(id = "a2", name = "黄色"),
        ChooseModel(id = "a3", name = "蓝色")
    )
    private val bSpecList = mutableListOf(
        ChooseModel(id = "b1", name = "帽子", checked = true),
        ChooseModel(id = "b2", name = "T恤"),
        ChooseModel(id = "b3", name = "裤子"),
        ChooseModel(id = "b4", name = "裙子")
    )
    private val cSpecList = mutableListOf(
        ChooseModel(id = "c1", name = "男士", checked = true),
        ChooseModel(id = "c2", name = "女士"),
        ChooseModel(id = "c3", name = "中性")
    )

    /**
     * 所有可选规格
     */
    private val allSpecList = mutableListOf(
        GroupChooseModel("规格1", aSpecList),
        GroupChooseModel("规格2", bSpecList),
        GroupChooseModel("规格3", cSpecList)
    )

    /**
     * 用户选取规格集合
     * 进入页面时用户已经选取了规格
     * 会有一个初始化规格
     */
    private var userCheckList = mutableListOf(
        ChooseModel(id = "a1", name = "红色", checked = true),
        ChooseModel(id = "b1", name = "帽子", checked = true),
        ChooseModel(id = "c1", name = "男士", checked = true)
    )

    /**
     * 可选规格列表view
     */
    private var parentLayoutModelList = mutableListOf<ParentLayoutModel>()

    /**
     * 所有可选规格列表
     */
    private val containCanSelectList: MutableList<List<List<ChooseModel>>> =
        mutableListOf(mutableListOf(), mutableListOf(), mutableListOf())

    /**
     * 所有用户能够选取的规格列表
     * fixme 取交集
     */
    private val userCanCheckList = mutableMapOf<Int, TreeSet<ChooseModel>>()

    /**
     * 所有的可选商品
     * 因为商品是规格的集合，此时一个商品即是几个规格的结合
     */
    private val skuList = mutableListOf<List<ChooseModel>>()

    private fun wareChooseInit() {

        shopList.forEach { spu ->
            val spuList = spu.split("-")
            val targetSpuList = mutableListOf<ChooseModel>()
            spuList.forEachIndexed { index, spuListItem ->
                val item = allSpecList[index].chooseModelList.find { it.id == spuListItem }
                if (item != null) {
                    targetSpuList.add(item)
                }
            }
            skuList.add(targetSpuList)
        }
        println("可选商品集合：$skuList")
        println()
    }

    /**
     * 所有的选取操作
     */
    private fun wareChoose() {
        /**
         * 所有规格数量
         */
        val selectCount = 3

        /**
         * 初始化可选列表
         */
        (0 until selectCount).forEach { index ->
//            userCheckList需要在此初始化
//            containCanSelectList也需要在此初始化
            userCanCheckList[index] = TreeSet()
        }


        /**
         * 根据用户选择的规格
         * 去展示其余剩下的可选规格
         */
        userCheckList.forEachIndexed { index, userCheckListItem ->

            if (userCheckListItem.id.isNotNullOrEmpty()) {
                /**
                 * 如果id不为空，则此规格处于选取状态
                 */
                val canSelectList = mutableListOf<List<ChooseModel>>()
                skuList.forEach { skuListItem ->
                    if (skuListItem.contains(userCheckListItem)) {
                        canSelectList.add(skuListItem)
                    }
                }
                println("canSelectList:$canSelectList")
                containCanSelectList[index] = canSelectList
            } else {
                /**
                 * 如果id为空则为未选取状态，此时需要将所有的商品都加入到列表中
                 */
                containCanSelectList[index] = skuList
            }
        }

        println("containCanSelectList:$containCanSelectList")

        val targetChooseSet = skuList.toMutableSet()

        val unionTargetChooseSet = TreeSet<List<ChooseModel>>()

        containCanSelectList.forEachIndexed { index, list ->
            println("需要去重：$list")
            println("去重之前的结果：$targetChooseSet")

            targetChooseSet.retainAll(list)

            unionTargetChooseSet.union(list)
        }

        println("去重之后的结果：$targetChooseSet")

        /**
         * 原始数据点选
         */
        skuList.toMutableSet().forEach { list ->
            list.forEachIndexed { innerIndex, chooseModel ->
//                userCanCheckList[innerIndex]?.add(chooseModel)
            }
        }

        /**
         * 去重之后取交集
         */
        targetChooseSet.forEach { list ->
            list.forEachIndexed { innerIndex, chooseModel ->
                userCanCheckList[innerIndex]?.add(chooseModel)
            }
        }

        /**
         * 取合集
         */
        unionTargetChooseSet.forEach { list ->
            list.forEachIndexed { innerIndex, chooseModel ->
//                userCanCheckList[innerIndex]?.add(chooseModel)
            }
        }

        /**
         * 此处为筛选去重后的列表
         * 也就是最后用户可选的列表
         */
        userCanCheckList.forEach { (position, chooseModelSet) ->
            println("可选列表$position")
            println(chooseModelSet.toString())

        }

        userCheckList.forEachIndexed { index, chooseModel ->
            println("第 $index 类已选规格：$chooseModel ")
        }

        /**
         * 此时的groupPosition是正确的
         * 但是childPosition需要去查找
         */
        userCanCheckList.forEach { (groupPosition, chooseModelSet) ->
            val canSelectList = mutableListOf<Int>()
            chooseModelSet.forEachIndexed { innerIndex, chooseModel ->
                /**
                 * 此时需要根据choosemodel的id去更新可选列表，如果包含则是可选，否则就是不可选
                 */
                val childList = allSpecList[groupPosition].chooseModelList
                val childPosition = childList.indexOfFirst { chooseModel.id == it.id }
                /**
                 * 查找到target
                 * 如果不等于-1即为目标target，此时需要加入到可选列表
                 * 将其他的置为不可选
                 */
                if (childPosition != -1) {
                    canSelectList.add(childPosition)
                    parentLayoutModelList[groupPosition].chooseView.notifyItem(
                        childPosition,
                        chooseModel
                    )
                }
                /**
                 * 当前筛选的item都是可选item
                 * 未进入筛选的都是不可选
                 */
                if (chooseModelSet.size - 1 == innerIndex) {
                    //此时需要将不可选全部置灰

                    allSpecList[groupPosition].chooseModelList.forEachIndexed { index, chooseModelInner ->
                        chooseModelInner.checkable = canSelectList.contains(index)
                        parentLayoutModelList[groupPosition].chooseView.notifyItem(
                            index,
                            chooseModelInner
                        )
                    }

                }
            }
        }
        println()
    }

    private fun javaAddView(context: Context) {
        wareChooseInit()
        allSpecList.forEachIndexed { outIndex, allSpecListItem ->
            val itemLinearLayout = LinearLayout(context)
            itemLinearLayout.tag = outIndex
            itemLinearLayout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            itemLinearLayout.orientation = LinearLayout.VERTICAL
            itemLinearLayout.setPadding(0, 12.dp2px, 0, 0)
            val itemTitle = TextView(context)
            //正常来说应在此处进行规格的选择
            itemTitle.text = allSpecListItem.title
            itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)
            itemTitle.setTextColor(Color.parseColor("#222222"))
            itemTitle.setPadding(5.dp2px, 0, 5.dp2px, 0)
            itemTitle.maxLines = 1
            itemTitle.ellipsize = TextUtils.TruncateAt.END
            itemTitle.gravity = Gravity.LEFT
            itemLinearLayout.addView(itemTitle)
            val itemFlowView = ChooseFlowView(context)
            itemFlowView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            allSpecListItem.chooseModelList.forEachIndexed { innerIndex, specItem ->
                val innerItem = SpecificationChooseView(context)
                innerItem.tag = innerIndex
                innerItem.chooseViewUnselected = R.drawable.choose_view_normal
                innerItem.setInnerTagLayoutParams(RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 32.dp2px))
                itemFlowView.addItemView(innerItem, specItem)
                itemFlowView.onItemClickListener =
                    { view, position, model, isCheckable, checkRepeat ->
                        //如果可选才可以点击，否则不能点击
                        if (isCheckable == true) {
                            //获取用户选取那个规格
                            val groupPosition = itemLinearLayout.tag as Int
                            val childPosition = innerItem.tag as Int
                            val chooseModel = (model as ChooseModel)
                            val isChecked = chooseModel.checked
                            if (isChecked) {
                                LogFactory.d("被选中 规格：" + allSpecList[groupPosition].title)
                                LogFactory.d("规格ITEM:" + chooseModel.name)
                                userCheckList[groupPosition] = chooseModel
                            } else {
                                LogFactory.d("取消选中 规格：" + allSpecList[groupPosition].title)
                                LogFactory.d("规格ITEM:" + chooseModel.name)
                                userCheckList[groupPosition] =
                                    ChooseModel(id = "", name = "", checked = false)
                            }
                            wareChoose()
                        }
                    }
            }
            itemLinearLayout.addView(itemFlowView)
            parentLayoutModelList.add(ParentLayoutModel(itemTitle, itemFlowView))

            chooseListLayout.addView(itemLinearLayout)
        }
        wareChoose()
    }
}

class ParentLayoutModel(var titleView: TextView, var chooseView: ChooseFlowView)

data class GroupChooseModel(var title: String? = null, var chooseModelList: List<ChooseModel>)

data class ChooseModel @JvmOverloads constructor(
    var name: String,//商品规格name
    var checkable: Boolean = true,
    var checked: Boolean = false,
    var id: String? = null//商品规格id
) :
    IChooseModel, Comparable<ChooseModel> {
    override fun getChooseItemTitle(): String {
        return name
    }

    override fun getChooseItemCheckable(): Boolean {
        return checkable
    }

    override fun getChooseItemChecked(): Boolean {
        return checked
    }

    override fun setChooseItemChecked(isChecked: Boolean) {
        checked = isChecked
    }

    override fun setChooseItemCheckable(checkable: Boolean) {
        this.checkable = checkable
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChooseModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun compareTo(other: ChooseModel): Int {
        return if (id == other.id) {
            0
        } else {
            1
        }
    }

    override fun toString(): String {
        return "$id"
    }


}
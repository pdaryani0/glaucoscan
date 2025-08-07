package com.glaucoma.ai.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.glaucoma.ai.BR
import com.glaucoma.ai.base.local.SharedPrefManager
import com.glaucoma.ai.base.utils.hideKeyboard
import javax.inject.Inject


abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {
    lateinit var binding: Binding
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    val parentActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateView(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.inflate(layoutInflater, layout,container,false)

        val vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        vm.onUnAuth.observe(viewLifecycleOwner) {
            val activity = requireActivity() as BaseActivity<*>
            activity.showUnauthorised()
        }
        return binding.root
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView(view: View)
    override fun onPause() {
        super.onPause()
        activity?.hideKeyboard()
    }
    fun showLoading(s: String?) {
        parentActivity?.showLoading()
    }

    fun showLoading(s: Int) {
        parentActivity?.showLoading()
    }
    fun hideLoading() {
        parentActivity?.hideLoading()
    }


}
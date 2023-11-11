package com.example.gizisight.ui.fragment.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.FragmentProfileBinding
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel // Replace with your ViewModel class name
    private lateinit var viewModelFactory: ViewModelFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ProfileViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        val sharedPref = SharedPrefManager(requireActivity())
        val token = sharedPref.getUser()


        viewModel.getUser("Bearer $token", sharedPref).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Toast.makeText(
                            requireActivity(),
                            "Loading..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        binding.apply {
                            tvName.text = result.data.username
                            tvUmur.text = result.data.age.toString() + " Tahun"
                            tvHeight.text = result.data.height.toString() + " CM"
                            tvWeight.text = result.data.weight.toString() + " Kg"
                            tvEmail.text = result.data.email
                            Glide.with(requireActivity()).load("https://ui-avatars.com/api/?name=${result.data.username}").circleCrop().into(imgProfile)
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            requireActivity(),
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct!!.displayName
            val personEmail = acct!!.email
            val personPhoto = acct!!.photoUrl

            binding.tvName.text = personName.toString()
            binding.tvEmail.text = personEmail.toString()
            Glide.with(requireActivity()).load(personPhoto).circleCrop().into(binding.imgProfile)
        }

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val sharedPref = SharedPrefManager(requireActivity())
            val token = sharedPref.removeToken()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().finish()
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
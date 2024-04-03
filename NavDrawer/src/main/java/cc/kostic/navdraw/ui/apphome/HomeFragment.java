package cc.kostic.navdraw.ui.apphome;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import cc.kostic.navdraw.R;
import cc.kostic.navdraw.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

	ActionMode actionMode;
	private FragmentHomeBinding binding;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

		binding = FragmentHomeBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		final TextView textView = binding.textHome;
		homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MenuHost mh = requireActivity();
		mh.addMenuProvider(ur_MenuNormal, getViewLifecycleOwner(), Lifecycle.State.RESUMED);	// FREEZE! kreiranje/unistavanje menu kad se ode u drugi navigation drawer -> vidi kako se poziva konstruktor za ViewPagerAdapter

		LifecycleOwner vlo = getViewLifecycleOwner();
		vlo.getLifecycle().addObserver(new DefaultLifecycleObserver() {
			@Override
			public void onPause(@NonNull LifecycleOwner owner) {
				DefaultLifecycleObserver.super.onPause(owner);
				// if (actionMode != null) {
					// Kada se ViewPager swajpuje na sledeci tab, javljam adapteru i zavrsavam action mode
					// Takodje izaziva kraj action moda kada se uredjaj rotira u jednu stranu i nazad
					// actionMode.finish();				// FREEZE
				// }
			}

		});
	}


	/////////////////////////////////////
	// Normalni menu
	/////////////////////////////////////
	private final MenuProvider ur_MenuNormal = new MenuProvider() {
		@Override
		public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
			// ovaj menu se pojavljuje samo u ovom fragmentu
			menuInflater.inflate(R.menu.menu_homefrag__toolbar, menu);
		}

		@Override
		public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
			int itemId = menuItem.getItemId();
			if (itemId == R.id.menu_homefragment_toolbar__settings) {
				// VAZNO !! Vidi themes.xml -> windowActionModeOverlay
				actionMode = requireActivity().startActionMode(ur_ContextualActionBar, ActionMode.TYPE_PRIMARY);	// vidi themes.xml -> windowActionModeOverlay
				// Log.d(TAG, "onMenuItemSelected() uredjajModel");
				// UredjajEdit_dlg dlg = UredjajEdit_dlg.newInstance(uredjajModel.getUredjajId());
				// dlg.show(getChildFragmentManager(), "uredjaj_edit");
				return true;
			}
			// if (itemId == R.id.menu_ur__button_settings) {
			// 	adapter.setStanje(Stanje.Edit);
			// 	actionMode = requireActivity().startActionMode(ur_ContextualActionBar, ActionMode.TYPE_PRIMARY);
			// 	return true;
			// }
			return false;
		}

		@Override
		public void onPrepareMenu(@NonNull Menu menu) {
			MenuProvider.super.onPrepareMenu(menu);
		}


		@Override
		public void onMenuClosed(@NonNull Menu menu) {
			MenuProvider.super.onMenuClosed(menu);
		}
	};



	/////////////////////////////////////
	// Dinamicki MENU tj ActionMode
	/////////////////////////////////////

	ActionMode.Callback ur_ContextualActionBar = new ActionMode.Callback(){

		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
			actionMode.getMenuInflater().inflate(R.menu.menu_homefrag_actionmode, menu);
			// actionMode.setTitle(getString(R.string.uredjaj_menu__editmode__title));
			// actionMode.setSubtitle(getString(R.string.uredjaj_menu__editmode__subtitle));
			// uredjajModel.setSuccessMsg(getString(R.string.uredjaj_menu__editmode__edit_buttons_hint));
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
			return false;
		}


		@Override
		public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
			int itemId = menuItem.getItemId();
			// if (itemId == R.id.menu_ur__actionmode__add_item) {
			// 	adapter.setStanje(Stanje.Add);
			// 	uredjajModel.setSuccessMsg(getString(R.string.common__add));
			// 	insert_dugme();
			// 	return true;
			// }
			// if (itemId == R.id.menu_ur__actionmode__edit_item) {
			// 	Toast.makeText(getActivity(), getString(R.string.uredjaj_menu__editmode__edit_buttons_hint), Toast.LENGTH_SHORT).show();
			// 	adapter.setStanje(Stanje.Edit);
			// 	uredjajModel.setSuccessMsg(getString(R.string.common__edit));
			// 	return true;
			// }
			// if (itemId == R.id.menu_ur__actionmode__sort) {
			// 	adapter.setStanje(Stanje.Drag);
			// 	uredjajModel.setSuccessMsg(getString(R.string.common__drag));
			// 	return true;
			// }
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			// adapter.setStanje(Stanje.None);
		}

	};



	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
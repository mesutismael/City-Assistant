package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.adapters.HabitantCategoryAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.HabitantContentProvider;
import smartassist.appreciate.be.smartassist.database.HabitantTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Habitant;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.views.KeyValueView;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class HabitantFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, HabitantCategoryAdapter.OnHabitantCategoryClickListener
{
    private CardView cardViewHabitant;
    private CardView cardViewMedicalInfo;
    private ImageView imageViewHabitant;
    private TextView textViewName;
    private KeyValueView textViewFlatNumber;
    private KeyValueView textViewBithday;
    private KeyValueView textViewInsuranceNumber;
    private KeyValueView textViewCivilState;
    private KeyValueView textViewPartner;
    private KeyValueView textViewPhone;
    private KeyValueView textViewEmail;
    private KeyValueView textViewStart;
    private KeyValueView textViewMutuality;
    private KeyValueView textViewLength;
    private KeyValueView textViewWeight;
    private KeyValueView textViewKatz;
    private KeyValueView textViewBel;
    private KeyValueView textViewDoctor;
    private KeyValueView textViewNurse;
    private KeyValueView textViewBloodType;
    private KeyValueView textViewIntolerances;
    private KeyValueView textViewAllergies;
    private KeyValueView textViewDiseases;
    private KeyValueView textViewAids;
    private KeyValueView textViewRemarks;
    private HabitantCategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_habitants, container, false);

        RecyclerView recyclerViewCategories = (RecyclerView) view.findViewById(R.id.recyclerView_categories);
        this.cardViewHabitant = (CardView) view.findViewById(R.id.cardView_habitant);
        this.cardViewMedicalInfo = (CardView) view.findViewById(R.id.cardView_medicalInfo);
        this.imageViewHabitant = (ImageView) view.findViewById(R.id.imageView_habitant);
        this.textViewName = (TextView) view.findViewById(R.id.textView_name);
        this.textViewFlatNumber = (KeyValueView) view.findViewById(R.id.keyValueView_flat_number);
        this.textViewBithday = (KeyValueView) view.findViewById(R.id.keyValueView_birth_day);
        this.textViewInsuranceNumber = (KeyValueView) view.findViewById(R.id.keyValueView_insuranceNumber);
        this.textViewCivilState = (KeyValueView) view.findViewById(R.id.keyValueView_civilState);
        this.textViewPartner = (KeyValueView) view.findViewById(R.id.keyValueView_partner);
        this.textViewPhone = (KeyValueView) view.findViewById(R.id.keyValueView_phone);
        this.textViewEmail = (KeyValueView) view.findViewById(R.id.keyValueView_email);
        this.textViewStart = (KeyValueView) view.findViewById(R.id.keyValueView_start);
        this.textViewMutuality = (KeyValueView) view.findViewById(R.id.keyValueView_mutuality);
        this.textViewLength = (KeyValueView) view.findViewById(R.id.keyValueView_length);
        this.textViewWeight = (KeyValueView) view.findViewById(R.id.keyValueView_weight);
        this.textViewKatz = (KeyValueView) view.findViewById(R.id.keyValueView_katz);
        this.textViewBel = (KeyValueView) view.findViewById(R.id.keyValueView_bel);
        this.textViewDoctor = (KeyValueView) view.findViewById(R.id.keyValueView_doctor);
        this.textViewNurse = (KeyValueView) view.findViewById(R.id.keyValueView_nurse);
        this.textViewBloodType = (KeyValueView) view.findViewById(R.id.keyValueView_bloodType);
        this.textViewIntolerances = (KeyValueView) view.findViewById(R.id.keyValueView_intolerances);
        this.textViewAllergies = (KeyValueView) view.findViewById(R.id.keyValueView_allergies);
        this.textViewDiseases = (KeyValueView) view.findViewById(R.id.keyValueView_diseases);
        this.textViewAids = (KeyValueView) view.findViewById(R.id.keyValueView_aids);
        this.textViewRemarks = (KeyValueView) view.findViewById(R.id.keyValueView_remarks);

        PaddingDecoration decorationCategories = new PaddingDecoration(view.getContext());
        recyclerViewCategories.addItemDecoration(decorationCategories);
        this.categoryAdapter = new HabitantCategoryAdapter();
        this.categoryAdapter.setListener(this);
        recyclerViewCategories.setAdapter(this.categoryAdapter);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().initLoader(Constants.LOADER_HABITANTS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_HABITANTS:
                String sortOrder = HabitantTable.COLUMN_NAME + " ASC";
                return new CursorLoader(this.getView().getContext(), HabitantContentProvider.CONTENT_URI, null, null, null, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_HABITANTS:
                List<Habitant> habitants = Habitant.constructListFromCursor(data);
                this.categoryAdapter.setHabitants(habitants);
                this.updateLayout(habitants != null && habitants.size() > 0 ? habitants.get(0) : null);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onHabitantCategoryClick(View caller, Habitant habitant)
    {
        this.updateLayout(habitant);
    }

    private void updateLayout(Habitant habitant)
    {
        if(habitant == null)
        {
            this.cardViewHabitant.setVisibility(View.GONE);
            this.cardViewMedicalInfo.setVisibility(View.GONE);
            return;
        }

        this.cardViewHabitant.setVisibility(View.VISIBLE);
        this.cardViewMedicalInfo.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(habitant.getPhoto()) && this.getActivity() != null)
        {
            this.imageViewHabitant.setVisibility(View.VISIBLE);
            Picasso.with(this.getActivity())
                    .load(habitant.getPhoto())
                    .into(this.imageViewHabitant);
        }
        else
        {
            this.imageViewHabitant.setVisibility(View.GONE);
        }
        this.textViewName.setText(habitant.getName());
        this.textViewFlatNumber.setValue(PreferencesHelper.getFlatNumber(getContext()));
        this.textViewBithday.setValue(DateUtils.formatHabitantDate(habitant.getBirthday()));
        this.textViewInsuranceNumber.setValue(habitant.getInsuranceNumber());
        this.textViewCivilState.setValue(habitant.getCivilState());
        this.textViewPartner.setValue(habitant.getPartner());
        this.textViewPhone.setValue(habitant.getPhone());
        this.textViewEmail.setValue(habitant.getEmail());
        this.textViewStart.setValue(DateUtils.formatHabitantDate(habitant.getStart()));
        this.textViewMutuality.setValue(habitant.getMutuality());
        this.textViewLength.setValue(habitant.getLength());
        this.textViewWeight.setValue(habitant.getWeight());
        this.textViewKatz.setValue(habitant.getKatz());
        this.textViewBel.setValue(habitant.getBel());
        this.textViewDoctor.setValue(habitant.getDoctor());
        this.textViewNurse.setValue(habitant.getNurse());
        this.textViewBloodType.setValue(habitant.getBloodType());
        this.textViewIntolerances.setValue(habitant.getIntolerances());
        this.textViewAllergies.setValue(habitant.getAllergies());
        this.textViewDiseases.setValue(habitant.getDiseases());
        this.textViewAids.setValue(habitant.getAids());
        this.textViewRemarks.setValue(habitant.getRemarks());
    }
}

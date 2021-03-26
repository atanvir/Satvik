package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.satvick.R;
import com.satvick.model.SubCategoriesModel;

import java.util.List;

/**
 * Created by Junaid on 7/7/2017.
 */

public class ParentLevelSupervisoryAdapter extends BaseExpandableListAdapter {
    private Context context;

    List<SubCategoriesModel> parentTeamMembersList;
    ExpandableListView expandListView;
    private int lastExpandedGroupPosition = -1;


    public ParentLevelSupervisoryAdapter(Context context, final List<SubCategoriesModel> parentTeamMembersList, final ExpandableListView expandListView) {
        this.context = context;
        this.parentTeamMembersList = parentTeamMembersList;
        this.expandListView = expandListView;

    }

    @Override
    public int getGroupCount() {
        return parentTeamMembersList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        //collapse the old expanded group, if not the same
        //as new group to expand
        if (groupPosition != lastExpandedGroupPosition) {
            expandListView.collapseGroup(lastExpandedGroupPosition);
        }
        super.onGroupExpanded(groupPosition);
        lastExpandedGroupPosition = groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_first, parent, false);
        }

//        TextView txtName = (TextView) view.findViewById(R.id.clientName);
//        TextView txtYtdDso = (TextView) view.findViewById(R.id.txtYtdDso);
//        TextView txtYtdIncentive = (TextView) view.findViewById(R.id.txtYtdIncentive);
//        TextView txtSupervisoryIncentive = (TextView) view.findViewById(R.id.txtSupervisoryIncentive);
//        TextView txtTerritory = (TextView) view.findViewById(R.id.txtTerritory);
//        CircleProgressView bpAchievementProgress = (CircleProgressView) view.findViewById(R.id.progressBpAchievement);
//        CircleProgressView salesReturnProgress = (CircleProgressView) view.findViewById(R.id.progressSalesReturn);
//        TextView supervisoryPvPointProgress = (TextView) view.findViewById(R.id.progressSupervisoryPoint);
//        SubCategoriesModel parentTeamMembers = parentTeamMembersList.get(groupPosition);
//
//
//        if (parentTeamMembers != null) {
//            if (parentTeamMembers.getName() != null) {
//                txtName.setText(parentTeamMembers.getName());
//            }
//            if (parentTeamMembers.getYtdDso() != null) {
//                txtYtdDso.setText(parentTeamMembers.getYtdDso() + "");
//            }
//            if (parentTeamMembers.getYtdDso() != null) {
//                txtYtdIncentive.setText(parentTeamMembers.getYtdIncentive() + "");
//            }
//            if (parentTeamMembers.getSupervisoryIncentive() != null) {
//                txtSupervisoryIncentive.setText(parentTeamMembers.getSupervisoryIncentive() + "");
//            }
//            if (parentTeamMembers.getTerriToryName() != null) {
//                txtTerritory.setText(parentTeamMembers.getTerriToryName() + "");
//            }
//            if (parentTeamMembers.getBpAchievement() != null && !parentTeamMembers.getBpAchievement().equals("")) {
//                if (parentTeamMembers.getBpAchievement() > 90) {
//                    bpAchievementProgress.setCircleColor(context.getResources().getColor(R.color.green));
//                    bpAchievementProgress.setTextColor(context.getResources().getColor(R.color.green));
//                } else if (parentTeamMembers.getBpAchievement() > 75 && parentTeamMembers.getBpAchievement() <= 90) {
//                    bpAchievementProgress.setCircleColor(context.getResources().getColor(R.color.light_yellow));
//                    bpAchievementProgress.setTextColor(context.getResources().getColor(R.color.grey));
//                } else if (parentTeamMembers.getBpAchievement() <= 75) {
//                    bpAchievementProgress.setCircleColor(context.getResources().getColor(R.color.red));
//                    bpAchievementProgress.setTextColor(context.getResources().getColor(R.color.red));
//                }
//
//
//                if (parentTeamMembers.getBpAchievement() != null) {
//                    bpAchievementProgress.setInterpolator(new AccelerateDecelerateInterpolator());
//                    bpAchievementProgress.setStartAngle(360);
//                    bpAchievementProgress.setProgressWithAnimation(parentTeamMembers.getBpAchievement(), 2000);
//                }
//            }
//            if (parentTeamMembers.getYtdSalesReturn() != null && !parentTeamMembers.getYtdSalesReturn().equals("")) {
//                if (parentTeamMembers.getYtdSalesReturn() < 3) {
//                    salesReturnProgress.setCircleColor(context.getResources().getColor(R.color.green));
//                    salesReturnProgress.setTextColor(context.getResources().getColor(R.color.green));
//                } else if (parentTeamMembers.getYtdSalesReturn() >= 3 && parentTeamMembers.getYtdSalesReturn() <= 4) {
//                    salesReturnProgress.setCircleColor(context.getResources().getColor(R.color.light_yellow));
//                    salesReturnProgress.setTextColor(context.getResources().getColor(R.color.grey));
//                } else if (parentTeamMembers.getYtdSalesReturn() > 4) {
//                    salesReturnProgress.setCircleColor(context.getResources().getColor(R.color.red));
//                    salesReturnProgress.setTextColor(context.getResources().getColor(R.color.red));
//                }
//                if (parentTeamMembers.getYtdSalesReturn() != null) {
//                    salesReturnProgress.setInterpolator(new AccelerateDecelerateInterpolator());
//                    salesReturnProgress.setStartAngle(360);
//                    salesReturnProgress.setProgressWithAnimation(parentTeamMembers.getYtdSalesReturn(), 2000);
//                }
//            }
//
//            if (parentTeamMembers.getSupervisoryPvPoint() != null && !parentTeamMembers.getSupervisoryPvPoint().equals("")) {
//                supervisoryPvPointProgress.setText(parentTeamMembers.getSupervisoryPvPoint() + "");
//               /* if (parentTeamMembers.getSupervisoryPvPoint() > 90) {
//                    supervisoryPvPointProgress.setCircleColor(context.getResources().getColor(R.color.green));
//                    supervisoryPvPointProgress.setTextColor(context.getResources().getColor(R.color.green));
//                } else if (parentTeamMembers.getSupervisoryPvPoint() < 75 && parentTeamMembers.getSupervisoryPvPoint() <= 90) {
//                    supervisoryPvPointProgress.setCircleColor(context.getResources().getColor(R.color.light_yellow));
//                    supervisoryPvPointProgress.setTextColor(context.getResources().getColor(R.color.light_yellow));
//                } else if (parentTeamMembers.getSupervisoryPvPoint() <= 75) {
//                    supervisoryPvPointProgress.setCircleColor(context.getResources().getColor(R.color.red));
//                    supervisoryPvPointProgress.setTextColor(context.getResources().getColor(R.color.red));
//                }
//                if (parentTeamMembers.getSupervisoryPvPoint() != null) {
//                    supervisoryPvPointProgress.setInterpolator(new AccelerateDecelerateInterpolator());
//                    supervisoryPvPointProgress.setStartAngle(360);
//                    supervisoryPvPointProgress.setProgressWithAnimation(Float.parseFloat(parentTeamMembers.getSupervisoryPvPoint()+""), 2000);
//                }*/
//            }
//
//
//        }


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SecondLevelExpandableListView secondLevelELV = null;
        if (secondLevelELV == null) {
            secondLevelELV = new SecondLevelExpandableListView(context);
        }
        secondLevelELV.setAdapter(new SecondLevelAdapter(context, parentTeamMembersList.get(groupPosition).getGetsubcategorylist()));
        secondLevelELV.setGroupIndicator(null);
        // convertView = secondLevelELV;
        return secondLevelELV;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class SecondLevelExpandableListView extends ExpandableListView {

        public SecondLevelExpandableListView(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    // Second Level Adapter
    public class SecondLevelAdapter extends BaseExpandableListAdapter {

        private Context context;
        List<SubCategoriesModel.Getsubcategorylist> parentTeamMembersList;

        public SecondLevelAdapter(Context context, List<SubCategoriesModel.Getsubcategorylist> parentTeamMembersList) {
            this.context = context;
            this.parentTeamMembersList = parentTeamMembersList;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return this.parentTeamMembersList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_second, parent, false);
                TextView tvBrand = (TextView) convertView.findViewById(R.id.rowSecondText);

                if (parentTeamMembersList.get(groupPosition).getSubcategory_name() != null) {
                    tvBrand.setText(parentTeamMembersList.get(groupPosition).getSubcategory_name());
                }
            }

            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.row_third, parent, false);
                }
                TextView txtName = (TextView) view.findViewById(R.id.rowThirdText);

//                if (parentTeamMembersList.get(groupPosition).getRemark_list().get(childPosition).getRemark_name() != null) {
//                    txtName.setText(parentTeamMembersList.get(groupPosition).getRemark_list().get(childPosition).getRemark_name());
//                }

            }
            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
           return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }

}

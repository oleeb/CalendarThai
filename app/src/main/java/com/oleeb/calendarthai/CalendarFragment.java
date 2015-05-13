package com.oleeb.calendarthai;

/**
 * Created by OLEEB on 6/25/2014.
 */

//public class CalendarFragment extends Fragment implements ViewPager.OnPageChangeListener {
//
//    private static final int START_YEAR = 1500;
//    private static final int END_YEAR = 2500;
//    private static final int TOTAL_MONTH = 12;
//    private static final int TOTAL_YEAR = END_YEAR-START_YEAR;
//    private static final int TOTAL_MONTH_YEAR = TOTAL_YEAR*TOTAL_MONTH;
//
//    private CalendarPagerAdapter mPagerAdapter;
//    private ViewPager mPager;
//    private TextView mTvNave;
//    private MenuItem mItemToDay;
//
//    private void setupCustomNavigator(){
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.navigator, null);
//        mTvNave = (TextView) view.findViewById(R.id.tvNav);
//        getActivity().getActionBar().setCustomView(view);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getActivity().setTitle(null);
//        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
//        setupCustomNavigator();
//        setHasOptionsMenu(true);
//        mPagerAdapter = new CalendarPagerAdapter(getActivity().getSupportFragmentManager());
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.itemToday:
//                setTodayPage();
//                return true;
//         default:
//             return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.month, menu);
//        mItemToDay = menu.findItem(R.id.itemToday);
//        setToday();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.pager, container, false);
//        mPager = (ViewPager) rootView.findViewById(R.id.pager);
//        mPager.setOnPageChangeListener(this);
//        mPager.setAdapter(mPagerAdapter);
//        setTodayPage();
//        return rootView;
//    }
//
//    private void setToday(){
//        Calendar calendar = Calendar.getInstance();
//        if(mItemToDay != null)
//            mItemToDay.setTitle(new SimpleDateFormat("d").format(calendar.getTime()));
//    }
//
//    private void setMonthPage(int selectedMonth){
//       mPager.setCurrentItem(getMonthPage(selectedMonth));
//    }
//
//    private void setTodayPage(){
//        mPager.setCurrentItem(getTodayPage());
//    }
//
//    private int getMonthPage(int selectedMonth){
//        return ((CalendarCurrent.getCurrentYear()-START_YEAR)*TOTAL_MONTH)+selectedMonth;
//    }
//
//    private int getTodayPage(){
//        return ((CalendarCurrent.getCurrentYear()-START_YEAR)*TOTAL_MONTH)+CalendarCurrent.getCurrentMonth();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float v, int i2) {
//        int[] cals = getCalendarByPosition(position);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, cals[0]);
//        calendar.set(Calendar.YEAR, cals[1]);
//        Log.d("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
//        mTvNave.setText(
//                new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
//    }
//
//    @Override
//    public void onPageSelected(int i) {
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int i) {
//
//    }
//
//    /**
//     * Page adapter
//     */
//    public class CalendarPagerAdapter extends FragmentPagerAdapter {
//
//        public CalendarPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            int[] cals = getCalendarByPosition(position);
//            return CalendarPagerFragment.newInstance(cals[0], cals[1]);
//        }
//
//        @Override
//        public int getCount() {
//            return TOTAL_MONTH_YEAR;
//        }
//    }
//
//    public static int[] getCalendarByPosition(int position){
//        // 0 = month, 1 = year
//        int[] cals = new int[2];
//        if(position < TOTAL_MONTH){
//            cals[0] = position;
//            cals[1] = START_YEAR;
//        }else{
//            cals[0] = position % TOTAL_MONTH;
//            cals[1] = START_YEAR + (position / TOTAL_MONTH);
//        }
//        return cals;
//    }
//
//    /**
//     * Month Page
//     */
//    public static class CalendarPagerFragment extends Fragment{
//        private int month;
//        private int year;
//        /**
//         * @return MonthPagerFragment
//         */
//        public static CalendarPagerFragment newInstance(int month, int year){
//            CalendarPagerFragment f = new CalendarPagerFragment();
//            Bundle b = new Bundle();
//            b.putInt("month", month);
//            b.putInt("year", year);
//            f.setArguments(b);
//            return f;
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            month = getArguments().getInt("month");
//            year = getArguments().getInt("year");
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            Calendar calendar = Calendar.getInstance(Locale.US);
//            calendar.set(year,month,1,0,0,0);
//            return new MonthViewWidget(getActivity(), calendar);
//        }
//    }
//}

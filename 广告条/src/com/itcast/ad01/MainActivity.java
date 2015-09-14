package com.itcast.ad01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 开发流程: 
 * 1. ViewPager简单使用 
 * 2. ViewPager循环滑动 
 * 3.增加标题显示,滑动页面修改标题内容
 * 4.小圆点初始化,页面切换,圆点变化 
 * 5.广告条自动轮播
 * 6.广告条触摸事件处理
 * 
 * @author Kevin
 * 
 */
public class MainActivity extends Activity {

	private ViewPager mViewPager;

	private int[] mImageIds = new int[] { R.drawable.a, R.drawable.b,
			R.drawable.c, R.drawable.d, R.drawable.e };

	// 图片标题集合
	private final String[] mImageDes = { "巩俐不低俗，我就不能低俗", "朴树又回来啦！再唱经典老歌引万人大合唱",
			"揭秘北京电影如何升级", "乐视网TV版大派送", "热血屌丝的反杀" };

	private TextView tvTitle;
	private LinearLayout llContainer;
	private int mLastPointPos = 0;// 上一个选中圆点的位置

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int currentItem = mViewPager.getCurrentItem();
			currentItem++;
			mViewPager.setCurrentItem(currentItem);// 切换到下一条广告

			mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,形成无限循环
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		mViewPager.setAdapter(new MyPagerAdapter());// 给ViewPager设置数据

		mViewPager.setCurrentItem(mImageIds.length * 1000);// 设置item的位置

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(mImageDes[0]);// 第一次初始化,设置标题内容

		// 设置页面滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// 某一页被选中
			@Override
			public void onPageSelected(int position) {
				int pos = position % mImageIds.length;
				tvTitle.setText(mImageDes[pos]);// 滑动后,修改标题内容

				llContainer.getChildAt(pos).setEnabled(true);// 被选中的圆点设置为红色
				llContainer.getChildAt(mLastPointPos).setEnabled(false);// 上一次被选中的圆点设置为灰色

				mLastPointPos = pos;// 给上一个圆点位置赋值
			}

			// 页面滑动事件
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			// 页面滑动状态发生变化
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		// 初始化小圆点
		llContainer = (LinearLayout) findViewById(R.id.ll_container);

		for (int i = 0; i < mImageIds.length; i++) {
			ImageView view = new ImageView(this);
			// view.setImageResource(R.drawable.shape_circle_default);
			view.setImageResource(R.drawable.shape_circle_selector);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (i != 0) {
				params.leftMargin = 5;// 从第二个圆点开始设置左边距
				view.setEnabled(false);// 从第二个圆点开始都设置为不可用
			}

			view.setLayoutParams(params);// 给ImageView设置布局参数

			llContainer.addView(view);
		}

		mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒发消息,切换广告条

		// 设置触摸事件
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 停止广告播放
					mHandler.removeCallbacksAndMessages(null);// 清除handler中的所有消息
					break;
				case MotionEvent.ACTION_UP:
					// 继续播放广告
					mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,形成无限循环
					break;

				default:
					break;
				}

				return false;// 要返回false,让ViewPager可以处理默认滑动效果的事件
			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		// 返回元素个数
		@Override
		public int getCount() {
			// return mImageIds.length;
			return Integer.MAX_VALUE;
		}

		// 判断要展示的view和返回的object是否是一个对象,如果是,才展示,一般都返回 view == object
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 初始化元素, ViewPager会默认自动初始化下一张图片, 保证使用屏幕中间一个布局,左右两侧一个布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(getApplicationContext());
			view.setBackgroundResource(mImageIds[position % mImageIds.length]);
			container.addView(view);// 将元素布局添加给view的容器

			System.out.println("初始化:" + position);

			return view;
		}

		// 销毁元素, ViewPager默认缓存3张图片, 会自动将多余图片销毁,销毁时会走此方法
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			System.out.println("销毁:" + position);
		}

	}

}

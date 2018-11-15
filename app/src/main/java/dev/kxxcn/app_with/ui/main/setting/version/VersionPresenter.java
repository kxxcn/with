package dev.kxxcn.app_with.ui.main.setting.version;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by kxxcn on 2018-10-30.
 */
public class VersionPresenter implements VersionContract.Presenter {

	private static final String PLAY_STORE = "https://play.google.com/store/apps/details?id=";
	private static final String SEPARATOR = ".htlgb";

	private VersionContract.View mVersionView;

	public VersionPresenter(VersionContract.View versionView) {
		this.mVersionView = versionView;
		mVersionView.setPresenter(this);
	}

	@Override
	public void checkVersion(String packageName) {
		new Thread() {
			@Override
			public void run() {
				if (mVersionView == null) {
					return;
				}
				try {
					Document doc = Jsoup.connect(PLAY_STORE + packageName).get();

					Elements Version = doc.select(SEPARATOR).eq(7);

					for (Element mElement : Version) {
						mVersionView.showSuccessfulyCheckVersion(mElement.text().trim());
					}
				} catch (IOException ex) {
					mVersionView.showUnsuccessfulyCheckVersion();
					ex.printStackTrace();
				}
			}
		}.start();
	}

}

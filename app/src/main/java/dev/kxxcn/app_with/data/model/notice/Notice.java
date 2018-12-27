package dev.kxxcn.app_with.data.model.notice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class Notice implements Parcelable {

	private int id;
	private String subject;
	private String content;
	private String date;

	protected Notice(Parcel in) {
		id = in.readInt();
		subject = in.readString();
		content = in.readString();
		date = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public static final Creator<Notice> CREATOR = new Creator<Notice>() {
		@Override
		public Notice createFromParcel(Parcel in) {
			return new Notice(in);
		}

		@Override
		public Notice[] newArray(int size) {
			return new Notice[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(subject);
		dest.writeString(content);
		dest.writeString(date);
	}

}

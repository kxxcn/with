package dev.kxxcn.app_with.data.model.plan;

/**
 * Created by kxxcn on 2018-10-08.
 */
public class Plan {

	private int id;
	private String writer;
	private String plan;
	private String place;
	private String time;
	private String date;

	public Plan(String writer, String plan, String place, String time, String date) {
		this.writer = writer;
		this.plan = plan;
		this.place = place;
		this.time = time;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public String getWriter() {
		return writer;
	}

	public String getPlan() {
		return plan;
	}

	public String getPlace() {
		return place;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

}

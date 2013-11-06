package com.capg.huaweiprofilestracker;

public class SensorData {
	public double ACCELEROMETER_X;
	public double ACCELEROMETER_Y;
	public double ACCELEROMETER_Z;
	public double GYROSCOPE_Roll;
	public double GYROSCOPE_Pitch;
	public double GYROSCOPE_Yaw;
	public double MAGNETIC_FIELD_X;
	public double MAGNETIC_FIELD_Y;
	public double MAGNETIC_FIELD_Z;
	public double ROTATION_VECTOR_X;
	public double ROTATION_VECTOR_Y;
	public double ROTATION_VECTOR_Z;
	public void set_ACCELEROMETER(float values[]) {
		ACCELEROMETER_X = values[0];
		ACCELEROMETER_Y = values[1];
		ACCELEROMETER_Z = values[2];
	}
	public void set_MAGNETIC_FIELD(float values[]) {
		MAGNETIC_FIELD_X = values[0];
		MAGNETIC_FIELD_Y = values[1];
		MAGNETIC_FIELD_Z = values[2];
	}
	public void set_ROTATION_VECTOR(float values[]) {
		ROTATION_VECTOR_X = values[0];
		ROTATION_VECTOR_Y = values[1];
		ROTATION_VECTOR_Z = values[2];
	}
	public void set_GYROSCOPE(float values[]) {
		GYROSCOPE_Roll = values[0];
		GYROSCOPE_Pitch = values[1];
		GYROSCOPE_Yaw = values[2];
	}
}

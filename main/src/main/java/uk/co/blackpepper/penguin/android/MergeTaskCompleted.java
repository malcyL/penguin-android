package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.client.ServiceException;

public interface MergeTaskCompleted
{
	void onTaskCompleted(ServiceException exception);
}

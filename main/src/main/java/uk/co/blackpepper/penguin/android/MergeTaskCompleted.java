package uk.co.blackpepper.penguin.android;

import java.util.List;

import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;

public interface MergeTaskCompleted
{
	void onTaskCompleted(Either<List<Story>, ServiceException> data);
}

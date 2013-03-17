package uk.co.blackpepper.penguin.android;

/**
 * Represents one type or another, inspired by Scala's Either.
 */
abstract class Either<L, R>
{
	public boolean isLeft()
	{
		return false;
	}
	
	public boolean isRight()
	{
		return false;
	}
	
	public L left()
	{
		throw new IllegalStateException("No left");
	}
	
	public R right()
	{
		throw new IllegalStateException("No right");
	}
	
	public static <L, R> Either<L, R> left(final L left)
	{
		return new Either<L, R>()
		{
			@Override
			public boolean isLeft()
			{
				return true;
			}
			
			@Override
			public L left()
			{
				return left;
			}
		};
	}
	
	public static <L, R> Either<L, R> right(final R right)
	{
		return new Either<L, R>()
		{
			@Override
			public boolean isRight()
			{
				return true;
			}
			
			@Override
			public R right()
			{
				return right;
			}
		};
	}
}

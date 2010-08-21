Just removed the Sender interface. SLF4J has no real flags. It simply has
methods with the expected flag names. Notice followed suit.

So, when you tell Notice that you want to `warn`, you call the `warn` method of
the NoticeFactory, to create a notice that will eventually record its message to
the SLF4J `warn` method.

There were no flags defined by SLF4J. It seemed like, if there were, that you
would record the flag, and then forward to a method like
`log(int level, String message)` but there are only methods, one for each type
for each name. That meant passing around a magic number, since levels are not
defined, I'd define them but only internally, and then a switch statement when
it comes time to send.

Of course, a switch statement is the sort of thing that makes a Java programmer
dispatch an army of objects, which is what I did.

So, I created a `Sender` object. This object contains the
`Logger` and has a method called `send` that is abstract. For each of the
methods in the `NoticeFactory` I created an anonymous inner class. It would call
the correct send method on the logger.

Except that my experience with trying to make Java small told me that I'd
introduced at least 5K to lieu of a switch statement.

Reivisting this code with my new found common sense, which tends to oppose the
authority, if not the conventional wisdom, of the Java community, anyway...

I used magic numbers 1 to 5 for TRACE to DEBUG and put in a switch statement to
save a lot of bytecode. Then it occured to me that the flags are defined in Java
logging, maybe I could use those flags? Sadly, no. There are a whole bunch of
differnet levels like `CONFIG` and `FINER`, which to not correlate to SLF4J and
are rather a bit much, so magic numbers it is.

Why magic numbers? Becuase 1 to 5 for TRACE to DEBUG is easy enough to document
for an internal variable, and I'm feeling stingy about bytecode bytes these
days. This should be a tiny library to encourage its adoption.

And I'm happy with my switch statement.

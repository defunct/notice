---
layout: default
title: Java and the Just World
---

{{ page.title }}
===

January 29th, 2010

I had an assertion in the reporting structure of NoticeException that asserted
that a map key was a valid Java identifier. That had gone away in other parts of
Notice. If you put a strange identifier into the map, it will be ignored by the
message formatting, but maybe you have your reasons.

Or more likely, it is a typo and a branch of code that you don't test or execute
often. In fact, most exception branches are the sorts of branches that you
hopefully will never execute outside of testing.

Generally, if something is amiss with a message in Notice, the programmer is
notified, not by raising an exception, but by altering the generate message. If
your message bundle can't be found, or if there is no message format for a given
key, we change the message to "Message bundle not found", or "No message found
for message key: invalid.name." This is because, you don't want to have to even
think about exception handling for your logging messages, or worse, your
exception messages. Throwing an exception raises an exception? Hateful.

I've spent enough time in Ruby now, that it occurs to me, the reason that most
APIs in Java are so painful to use are precisely because Java programmers tend
to subscribe to that conserviative philosophy of the Just World Theory, which is
a theory where the world is naturally just. Thus, any rewards you receive in
life must have been deserved, any fate the befalls you must have been deserved.

It is a philosophy that protectes the very rich, because if they were bad
people, they would not be very rich, because that is unfair and the world is
naturally just. It is a philosophy that condems the very poor, because if they
were good people, they would not be very poor, and the naturally just world
would see that they are rewarded.

The common rationalization for a very poor person is to say, you say you can't
afford your cancer treatments, but you still own your house. Sell your house and
then maybe I'll feel sorry for you. But, of course, once the house is sold, they
have no house and no medical attention, and the rational is that you brought
this upon yourself by not purchasing enough health insurance. 

You brought this upon yourself. That is the attitude of the Java API developer.
If you are foolish enough to use my library without testing every line of code,
they you deserve to face cryptic error messages when I raise an exception while
you are raising an exception. That would be the attitude to take in the
development of this exception library.

It is suprising how often it is indeed the attitude that Java programmers take,
and you can't shake them off of it. I believe it is in part due to the
responsiblity shirking that comes with checked exceptions. I told you that I
might throw this exception. You need to come up with a test strategy for every
possible error condition. If you don't, you get what you deserve.

What people deserve are libraries that do not treat programmer errors a failings
of character. Libraries that make it easy to diagnose and rectify common
misktakes.

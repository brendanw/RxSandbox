package async

/**
 * Action priorities that must be declared in descending order
 *
 * Whichever priority is defined earliest here will be considered
 * highest priority in the queue, regardless of name or numerical value.
 *
 * threadPriority is used to set the priority level of tasks created with
 * this priority
 *
 * @see Action.run
 */
enum class Priority (val threadPriority: Int) {
    IMMEDIATE(Thread.MAX_PRIORITY),
    HIGH(Thread.NORM_PRIORITY),
    NORMAL(Thread.NORM_PRIORITY),
    DEFAULT(Thread.MIN_PRIORITY);


    companion object {
        fun fromValue(value: Int): Priority {
            for (priority in values()) {
                if (priority.ordinal == value) {
                    return priority
                }
            }
            throw IllegalStateException("No async.Priority exists with ordinal value: $value")
        }
    }

}

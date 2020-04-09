package com.pavelhabzansky.infrastructure.features.logfile

class FiFoList<T> {
    private var length: Int = 0
    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    init {
        length = 0
        head = null
        tail = null
    }

    fun length(): Int {
        return this.length
    }

    fun add(value: T) {
        val n = Node<T>(value, head)
        if (tail == null) {
            tail = n
            if (length != 0) {
                println("(add) Buffer wrong length: " + length)
                length = 0
            }
        }
        if (head != null) {
            head!!.prew = n
        }
        head = n
        ++length

        //System.out.println("add[" + length + "]: <" + n.msg + ">");
    }

    fun get(): T? {
        if (tail == null) {
            if (length != 0) {
                println("(get) Buffer wrong length: " + length)
            }
            length = 0
            return null
        }

        val n = tail
        --length
        //System.out.println("get[" + length + "]: <" + n.msg + "> n.next = " + n.next + "; n.prew = " + n.prew + ";");

        if (n != null) {
            tail = n.prew
            n.prew = null
            if (tail != null) {
                tail!!.next = null
            }
        }
        return n!!.value
    }

    private inner class Node<V>(internal val value: V, internal var next: Node<V>?) {
        internal var prew: Node<V>? = null

        init {
            this.prew = null
        }
    }

    override fun toString(): String {
        return "FifoList .size = ${length} / first element = ${head}"
    }
}
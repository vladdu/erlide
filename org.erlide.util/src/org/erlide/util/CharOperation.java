/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.erlide.util;

/**
 * This class is a collection of helper methods to manipulate char arrays.
 *
 *
 */
public final class CharOperation {

    private CharOperation() {
    }

    /**
     * Constant for an empty char array
     */
    public static final char[] NO_CHAR = new char[0];

    /**
     * Constant for an empty char array with two dimensions.
     */
    public static final char[][] NO_CHAR_CHAR = new char[0][];

    /**
     * Answers a new array with appending the suffix character at the end of the
     * array. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     *   array = { 'a', 'b' }
     *   suffix = 'c'
     * =&gt; result = { 'a', 'b' , 'c' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     *   array = null
     *   suffix = 'c'
     * =&gt; result = { 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the array that is concanated with the suffix character
     * @param suffix
     *            the suffix character
     * @return the new array
     */
    public static char[] append(final char[] array, final char suffix) {
        if (array == null) {
            return new char[] { suffix };
        }
        final int length = array.length;
        final char[] arr = new char[length + 1];
        System.arraycopy(array, 0, arr, 0, length);
        arr[length] = suffix;
        return arr;
    }

    /**
     * Append the given subarray to the target array starting at the given index
     * in the target array. The start of the subarray is inclusive, the end is
     * exclusive. Answers a new target array if it needs to grow, otherwise
     * answers the same target array. <br>
     * For example: <br>
     * <ol>
     * <li>
     *
     * <pre>
     * target = { 'a', 'b', '0' }
     *                index = 2
     *                array = { 'c', 'd' }
     *                start = 0
     *                end = 1
     *                =&gt; result = { 'a', 'b' , 'c' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * target = { 'a', 'b' }
     *                index = 2
     *                array = { 'c', 'd' }
     *                start = 0
     *                end = 1
     *                =&gt; result = { 'a', 'b' , 'c', '0', '0' , '0' } (new array)
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * target = { 'a', 'b', 'c' }
     *                index = 1
     *                array = { 'c', 'd', 'e', 'f' }
     *                start = 1
     *                end = 4
     *                =&gt; result = { 'a', 'd' , 'e', 'f', '0', '0', '0', '0' } (new array)
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param target
     *            the given target
     * @param index
     *            the given index
     * @param array
     *            the given array
     * @param start
     *            the given start index
     * @param end
     *            the given end index
     *
     * @return the new array
     * @throws NullPointerException
     *             if the target array is null
     */
    public static char[] append(final char[] target0, final int index,
            final char[] array, final int start, final int end) {
        char[] target = target0;
        final int targetLength = target.length;
        final int subLength = end - start;
        final int newTargetLength = subLength + index;
        if (newTargetLength > targetLength) {
            System.arraycopy(target, 0, target = new char[newTargetLength * 2], 0, index);
        }
        System.arraycopy(array, start, target, index, subLength);
        return target;
    }

    /**
     * Answers the concatenation of the two arrays. It answers null if the two
     * arrays are null. If the first array is null, then the second array is
     * returned. If the second array is null, then the first array is returned. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                =&gt; result = null
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { ' a' } }
     *                second = null
     *                =&gt; result = { { ' a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = null
     *                second = { { ' a' } }
     *                =&gt; result = { { ' a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { ' b' } }
     *                second = { { ' a' } }
     *                =&gt; result = { { ' b' }, { ' a' } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param second
     *            the second array to concatenate
     * @return the concatenation of the two arrays, or null if the two arrays
     *         are null.
     */
    public static char[][] arrayConcat(final char[][] first, final char[][] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }

        final int length1 = first.length;
        final int length2 = second.length;
        final char[][] result = new char[length1 + length2][];
        System.arraycopy(first, 0, result, 0, length1);
        System.arraycopy(second, 0, result, length1, length2);
        return result;
    }

    /**
     * Returns the char arrays as an array of Strings
     *
     * @param charArrays
     *            the char array to convert
     * @return the char arrays as an array of Strings or null if the given char
     *         arrays is null.
     *
     */
    public static String[] charArrayToStringArray(final char[][] charArrays) {
        if (charArrays == null) {
            return null;
        }
        final String[] strings = new String[charArrays.length];
        for (int i = 0; i < charArrays.length; i++) {
            strings[i] = new String(charArrays[i]);
        }
        return strings;
    }

    /**
     * Returns the char array as a String
     *
     * @param charArray
     *            the char array to convert
     * @return the char array as a String or null if the given char array is
     *         null.
     *
     */
    public static String charToString(final char[] charArray) {
        if (charArray == null) {
            return null;
        }
        return new String(charArray);
    }

    /**
     * Answers a new array adding the second array at the end of first array. It
     * answers null if the first and second are null. If the first array is
     * null, then a new array char[][] is created with second. If the second
     * array is null, then the first array is returned. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = { 'a' }
     *                =&gt; result = { { ' a' } }
     * </pre>
     *
     * <li>
     *
     * <pre>
     * first = { { ' a' } }
     *                second = null
     *                =&gt; result = { { ' a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { ' a' } }
     *                second = { ' b' }
     *                =&gt; result = { { ' a' } , { ' b' } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param second
     *            the array to add at the end of the first array
     * @return a new array adding the second array at the end of first array, or
     *         null if the two arrays are null.
     */
    public static char[][] arrayConcat(final char[][] first, final char[] second) {
        if (second == null) {
            return first;
        }
        if (first == null) {
            return new char[][] { second };
        }

        final int length = first.length;
        final char[][] result = new char[length + 1][];
        System.arraycopy(first, 0, result, 0, length);
        result[length] = second;
        return result;
    }

    /**
     * Compares the contents of the two arrays array and prefix. Returns
     * <ul>
     * <li>zero if the array starts with the prefix contents</li>
     * <li>the difference between the first two characters that are not equal</li>
     * <li>one if array length is lower than the prefix length and that the
     * prefix starts with the array contents.</li>
     * </ul>
     * <p>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = null
     *                prefix = null
     *                =&gt; result = NullPointerException
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b', 'c', 'd', 'e' }
     *                prefix = { 'a', 'b', 'c'}
     *                =&gt; result = 0
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b', 'c', 'd', 'e' }
     *                prefix = { 'a', 'B', 'c'}
     *                =&gt; result = 32
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'd', 'b', 'c', 'd', 'e' }
     *                prefix = { 'a', 'b', 'c'}
     *                =&gt; result = 3
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b', 'c', 'd', 'e' }
     *                prefix = { 'd', 'b', 'c'}
     *                =&gt; result = -3
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'a', 'c', 'd', 'e' }
     *                prefix = { 'a', 'e', 'c'}
     *                =&gt; result = -4
     * </pre>
     *
     * </li>
     * </ol>
     * </p>
     *
     * @param array
     *            the given array
     * @param prefix
     *            the given prefix
     * @return the result of the comparison (>=0 if array>prefix)
     * @throws NullPointerException
     *             if either array or prefix is null
     */
    public static int compareWith(final char[] array, final char[] prefix) {
        final int arrayLength = array.length;
        final int prefixLength = prefix.length;
        int min = Math.min(arrayLength, prefixLength);
        int i = 0;
        while (min-- != 0) {
            final char c1 = array[i];
            final char c2 = prefix[i++];
            if (c1 != c2) {
                return c1 - c2;
            }
        }
        if (prefixLength == i) {
            return 0;
        }
        return -1; // array is shorter than prefix (e.g. array:'ab' <
        // prefix:'abc').
    }

    /**
     * Answers the concatenation of the two arrays. It answers null if the two
     * arrays are null. If the first array is null, then the second array is
     * returned. If the second array is null, then the first array is returned. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = { 'a' }
     *                =&gt; result = { ' a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { ' a' }
     *                second = null
     *                =&gt; result = { ' a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { ' a' }
     *                second = { ' b' }
     *                =&gt; result = { ' a' , ' b' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param second
     *            the second array to concatenate
     * @return the concatenation of the two arrays, or null if the two arrays
     *         are null.
     */
    public static char[] concat(final char[] first, final char[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }

        final int length1 = first.length;
        final int length2 = second.length;
        final char[] result = new char[length1 + length2];
        System.arraycopy(first, 0, result, 0, length1);
        System.arraycopy(second, 0, result, length1, length2);
        return result;
    }

    /**
     * Answers the concatenation of the three arrays. It answers null if the
     * three arrays are null. If first is null, it answers the concatenation of
     * second and third. If second is null, it answers the concatenation of
     * first and third. If third is null, it answers the concatenation of first
     * and second. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = { 'a' }
     *                third = { 'b' }
     *                =&gt; result = { ' a', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = null
     *                third = { 'b' }
     *                =&gt; result = { ' a', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'b' }
     *                third = null
     *                =&gt; result = { ' a', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                third = null
     *                =&gt; result = null
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'b' }
     *                third = { 'c' }
     *                =&gt; result = { 'a', 'b', 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param second
     *            the second array to concatenate
     * @param third
     *            the third array to concatenate
     *
     * @return the concatenation of the three arrays, or null if the three
     *         arrays are null.
     */
    public static char[] concat(final char[] first, final char[] second,
            final char[] third) {
        if (first == null) {
            return concat(second, third);
        }
        if (second == null) {
            return concat(first, third);
        }
        if (third == null) {
            return concat(first, second);
        }

        final int length1 = first.length;
        final int length2 = second.length;
        final int length3 = third.length;
        final char[] result = new char[length1 + length2 + length3];
        System.arraycopy(first, 0, result, 0, length1);
        System.arraycopy(second, 0, result, length1, length2);
        System.arraycopy(third, 0, result, length1 + length2, length3);
        return result;
    }

    /**
     * Answers the concatenation of the two arrays inserting the separator
     * character between the two arrays. It answers null if the two arrays are
     * null. If the first array is null, then the second array is returned. If
     * the second array is null, then the first array is returned. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = { 'a' }
     *                separator = '/'
     *                =&gt; result = { ' a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { ' a' }
     *                second = null
     *                separator = '/'
     *                =&gt; result = { ' a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { ' a' }
     *                second = { ' b' }
     *                separator = '/'
     *                =&gt; result = { ' a' , '/', 'b' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param second
     *            the second array to concatenate
     * @param separator
     *            the character to insert
     * @return the concatenation of the two arrays inserting the separator
     *         character between the two arrays , or null if the two arrays are
     *         null.
     */
    public static char[] concat(final char[] first, final char[] second,
            final char separator) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }

        final int length1 = first.length;
        if (length1 == 0) {
            return second;
        }
        final int length2 = second.length;
        if (length2 == 0) {
            return first;
        }

        final char[] result = new char[length1 + length2 + 1];
        System.arraycopy(first, 0, result, 0, length1);
        result[length1] = separator;
        System.arraycopy(second, 0, result, length1 + 1, length2);
        return result;
    }

    /**
     * Answers the concatenation of the three arrays inserting the sep1
     * character between the two arrays and sep2 between the last two. It
     * answers null if the three arrays are null. If the first array is null,
     * then it answers the concatenation of second and third inserting the sep2
     * character between them. If the second array is null, then it answers the
     * concatenation of first and third inserting the sep1 character between
     * them. If the third array is null, then it answers the concatenation of
     * first and second inserting the sep1 character between them. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                sep1 = '/'
     *                second = { 'a' }
     *                sep2 = ':'
     *                third = { 'b' }
     *                =&gt; result = { ' a' , ':', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                sep1 = '/'
     *                second = null
     *                sep2 = ':'
     *                third = { 'b' }
     *                =&gt; result = { ' a' , '/', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                sep1 = '/'
     *                second = { 'b' }
     *                sep2 = ':'
     *                third = null
     *                =&gt; result = { ' a' , '/', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                sep1 = '/'
     *                second = { 'b' }
     *                sep2 = ':'
     *                third = { 'c' }
     *                =&gt; result = { ' a' , '/', 'b' , ':', 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array to concatenate
     * @param sep1
     *            the character to insert
     * @param second
     *            the second array to concatenate
     * @param sep2
     *            the character to insert
     * @param third
     *            the second array to concatenate
     * @return the concatenation of the three arrays inserting the sep1
     *         character between the two arrays and sep2 between the last two.
     */
    public static char[] concat(final char[] first, final char sep1, final char[] second,
            final char sep2, final char[] third) {
        if (first == null) {
            return concat(second, third, sep2);
        }
        if (second == null) {
            return concat(first, third, sep1);
        }
        if (third == null) {
            return concat(first, second, sep1);
        }

        final int length1 = first.length;
        final int length2 = second.length;
        final int length3 = third.length;
        final char[] result = new char[length1 + length2 + length3 + 2];
        System.arraycopy(first, 0, result, 0, length1);
        result[length1] = sep1;
        System.arraycopy(second, 0, result, length1 + 1, length2);
        result[length1 + length2 + 1] = sep2;
        System.arraycopy(third, 0, result, length1 + length2 + 2, length3);
        return result;
    }

    /**
     * Answers a new array with prepending the prefix character and appending
     * the suffix character at the end of the array. If array is null, it
     * answers a new array containing the prefix and the suffix characters. <br>
     * <br>
     * For example: <br>
     * <ol>
     * <li>
     *
     * <pre>
     * prefix = 'a'
     *                array = { 'b' }
     *                suffix = 'c'
     *                =&gt; result = { 'a', 'b' , 'c' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * prefix = 'a'
     *                array = null
     *                suffix = 'c'
     *                =&gt; result = { 'a', 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param prefix
     *            the prefix character
     * @param array
     *            the array that is concanated with the prefix and suffix
     *            characters
     * @param suffix
     *            the suffix character
     * @return the new array
     */
    public static char[] concat(final char prefix, final char[] array, final char suffix) {
        if (array == null) {
            return new char[] { prefix, suffix };
        }

        final int length = array.length;
        final char[] result = new char[length + 2];
        result[0] = prefix;
        System.arraycopy(array, 0, result, 1, length);
        result[length + 1] = suffix;
        return result;
    }

    /**
     * Answers the concatenation of the given array parts using the given
     * separator between each part and appending the given name at the end. <br>
     * <br>
     * For example: <br>
     * <ol>
     * <li>
     *
     * <pre>
     * name = { 'c' }
     *                array = { { 'a' }, { 'b' } }
     *                separator = '.'
     *                =&gt; result = { 'a', '.', 'b' , '.', 'c' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * name = null
     *                array = { { 'a' }, { 'b' } }
     *                separator = '.'
     *                =&gt; result = { 'a', '.', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * name = { ' c' }
     *                array = null
     *                separator = '.'
     *                =&gt; result = { 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param name
     *            the given name
     * @param array
     *            the given array
     * @param separator
     *            the given separator
     * @return the concatenation of the given array parts using the given
     *         separator between each part and appending the given name at the
     *         end
     */
    public static char[] concatWith(final char[] name, final char[][] array,
            final char separator) {
        return concatWith(array, name, separator);
    }

    /**
     * Answers the concatenation of the given array parts using the given
     * separator between each part and appending the given name at the end. <br>
     * <br>
     * For example: <br>
     * <ol>
     * <li>
     *
     * <pre>
     * name = { 'c' }
     *                array = { { 'a' }, { 'b' } }
     *                separator = '.'
     *                =&gt; result = { 'a', '.', 'b' , '.', 'c' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * name = null
     *                array = { { 'a' }, { 'b' } }
     *                separator = '.'
     *                =&gt; result = { 'a', '.', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * name = { ' c' }
     *                array = null
     *                separator = '.'
     *                =&gt; result = { 'c' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param name
     *            the given name
     * @param separator
     *            the given separator
     * @return the concatenation of the given array parts using the given
     *         separator between each part and appending the given name at the
     *         end
     */
    @SuppressWarnings("null")
    public static char[] concatWith(final char[][] array, final char[] name,
            final char separator) {
        final int nameLength = name == null ? 0 : name.length;
        if (nameLength == 0) {
            return concatWith(array, separator);
        }

        final int length = array == null ? 0 : array.length;
        if (length == 0) {
            return name;
        }

        int size = nameLength;
        int index = length;
        while (--index >= 0) {
            if (array[index].length > 0) {
                size += array[index].length + 1;
            }
        }
        final char[] result = new char[size];
        index = 0;
        for (int i = 0; i < length; i++) {
            final int subLength = array[i].length;
            if (subLength > 0) {
                System.arraycopy(array[i], 0, result, index, subLength);
                index += subLength;
                result[index++] = separator;
            }
        }
        System.arraycopy(name, 0, result, index, nameLength);
        return result;
    }

    /**
     * Answers the concatenation of the given array parts using the given
     * separator between each part. <br>
     * <br>
     * For example: <br>
     * <ol>
     * <li>
     *
     * <pre>
     * array = { { 'a' }, { 'b' } }
     *                separator = '.'
     *                =&gt; result = { 'a', '.', 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = null
     *                separator = '.'
     *                =&gt; result = { }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param separator
     *            the given separator
     * @return the concatenation of the given array parts using the given
     *         separator between each part
     */
    @SuppressWarnings("null")
    public static char[] concatWith(final char[][] array, final char separator) {
        int length = array == null ? 0 : array.length;
        if (length == 0) {
            return CharOperation.NO_CHAR;
        }

        int size = length - 1;
        int index = length;
        while (--index >= 0) {
            if (array[index].length == 0) {
                size--;
            } else {
                size += array[index].length;
            }
        }
        if (size <= 0) {
            return CharOperation.NO_CHAR;
        }
        final char[] result = new char[size];
        index = length;
        while (--index >= 0) {
            length = array[index].length;
            if (length > 0) {
                System.arraycopy(array[index], 0, result, size -= length, length);
                if (--size >= 0) {
                    result[size] = separator;
                }
            }
        }
        return result;
    }

    /**
     * Answers true if the array contains an occurrence of character, false
     * otherwise. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * character = 'c'
     *                array = { { ' a' }, { ' b' } }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * character = 'a'
     *                array = { { ' a' }, { ' b' } }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param character
     *            the character to search
     * @param array
     *            the array in which the search is done
     * @return true if the array contains an occurrence of character, false
     *         otherwise.
     * @throws NullPointerException
     *             if array is null.
     */
    public static boolean contains(final char character, final char[][] array) {
        for (int i = array.length; --i >= 0;) {
            final char[] subarray = array[i];
            for (int j = subarray.length; --j >= 0;) {
                if (subarray[j] == character) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Answers true if the array contains an occurrence of character, false
     * otherwise. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * character = 'c'
     *                array = { ' b'  }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * character = 'a'
     *                array = { ' a' , ' b' }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param character
     *            the character to search
     * @param array
     *            the array in which the search is done
     * @return true if the array contains an occurrence of character, false
     *         otherwise.
     * @throws NullPointerException
     *             if array is null.
     */
    public static boolean contains(final char character, final char[] array) {
        for (int i = array.length; --i >= 0;) {
            if (array[i] == character) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answers a deep copy of the toCopy array.
     *
     * @param toCopy
     *            the array to copy
     * @return a deep copy of the toCopy array.
     */
    public static char[][] deepCopy(final char[][] toCopy) {
        final int toCopyLength = toCopy.length;
        final char[][] result = new char[toCopyLength][];
        for (int i = 0; i < toCopyLength; i++) {
            final char[] toElement = toCopy[i];
            final int toElementLength = toElement.length;
            final char[] resultElement = new char[toElementLength];
            System.arraycopy(toElement, 0, resultElement, 0, toElementLength);
            result[i] = resultElement;
        }
        return result;
    }

    /**
     * Return true if array ends with the sequence of characters contained in
     * toBeFound, otherwise false. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b', 'c', 'd' }
     *                toBeFound = { 'b', 'c' }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b', 'c' }
     *                toBeFound = { 'b', 'c' }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the array to check
     * @param toBeFound
     *            the array to find
     * @return true if array ends with the sequence of characters contained in
     *         toBeFound, otherwise false.
     * @throws NullPointerException
     *             if array is null or toBeFound is null
     */
    public static boolean endsWith(final char[] array, final char[] toBeFound) {
        int i = toBeFound.length;
        final int j = array.length - i;

        if (j < 0) {
            return false;
        }
        while (--i >= 0) {
            if (toBeFound[i] != array[i + j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Answers true if the two arrays are identical character by character,
     * otherwise false. The equality is case sensitive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { } }
     *                second = null
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { 'a' } }
     *                second = { { 'a' } }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { 'A' } }
     *                second = { { 'a' } }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array
     * @param second
     *            the second array
     * @return true if the two arrays are identical character by character,
     *         otherwise false
     */
    public static boolean equals(final char[][] first, final char[][] second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (!equals(first[i], second[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * If isCaseSensite is true, answers true if the two arrays are identical
     * character by character, otherwise false. If it is false, answers true if
     * the two arrays are identical character by character without checking the
     * case, otherwise false. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                isCaseSensitive = true
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { } }
     *                second = null
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { 'A' } }
     *                second = { { 'a' } }
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { { 'A' } }
     *                second = { { 'a' } }
     *                isCaseSensitive = false
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array
     * @param second
     *            the second array
     * @param isCaseSensitive
     *            check whether or not the equality should be case sensitive
     * @return true if the two arrays are identical character by character
     *         according to the value of isCaseSensitive, otherwise false
     */
    public static boolean equals(final char[][] first, final char[][] second,
            final boolean isCaseSensitive) {

        if (isCaseSensitive) {
            return equals(first, second);
        }
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (!equals(first[i], second[i], false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Answers true if the two arrays are identical character by character,
     * otherwise false. The equality is case sensitive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { }
     *                second = null
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'a' }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'A' }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array
     * @param second
     *            the second array
     * @return true if the two arrays are identical character by character,
     *         otherwise false
     */
    public static boolean equals(final char[] first, final char[] second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (first[i] != second[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Answers true if the first array is identical character by character to a
     * portion of the second array delimited from position secondStart
     * (inclusive) to secondEnd(exclusive), otherwise false. The equality is
     * case sensitive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                secondStart = 0
     *                secondEnd = 0
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { }
     *                second = null
     *                secondStart = 0
     *                secondEnd = 0
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'a' }
     *                secondStart = 0
     *                secondEnd = 1
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'a' }
     *                second = { 'A' }
     *                secondStart = 0
     *                secondEnd = 1
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array
     * @param second
     *            the second array
     * @param secondStart
     *            inclusive start position in the second array to compare
     * @param secondEnd
     *            exclusive end position in the second array to compare
     * @return true if the first array is identical character by character to
     *         fragment of second array ranging from secondStart to secondEnd-1,
     *         otherwise false
     *
     */
    public static boolean equals(final char[] first, final char[] second,
            final int secondStart, final int secondEnd) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != secondEnd - secondStart) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (first[i] != second[i + secondStart]) {
                return false;
            }
        }
        return true;
    }

    /**
     * If isCaseSensite is true, answers true if the two arrays are identical
     * character by character, otherwise false. If it is false, answers true if
     * the two arrays are identical character by character without checking the
     * case, otherwise false. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * first = null
     *                second = null
     *                isCaseSensitive = true
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { }
     *                second = null
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'A' }
     *                second = { 'a' }
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * first = { 'A' }
     *                second = { 'a' }
     *                isCaseSensitive = false
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param first
     *            the first array
     * @param second
     *            the second array
     * @param isCaseSensitive
     *            check whether or not the equality should be case sensitive
     * @return true if the two arrays are identical character by character
     *         according to the value of isCaseSensitive, otherwise false
     */
    public static boolean equals(final char[] first, final char[] second,
            final boolean isCaseSensitive) {

        if (isCaseSensitive) {
            return equals(first, second);
        }
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }

        for (int i = first.length; --i >= 0;) {
            if (Character.toLowerCase(first[i]) != Character.toLowerCase(second[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * If isCaseSensite is true, the equality is case sensitive, otherwise it is
     * case insensitive.
     *
     * Answers true if the name contains the fragment at the starting index
     * startIndex, otherwise false. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * fragment = { 'b', 'c' , 'd' }
     *                name = { 'a', 'b', 'c' , 'd' }
     *                startIndex = 1
     *                isCaseSensitive = true
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * fragment = { 'b', 'c' , 'd' }
     *                name = { 'a', 'b', 'C' , 'd' }
     *                startIndex = 1
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * fragment = { 'b', 'c' , 'd' }
     *                name = { 'a', 'b', 'C' , 'd' }
     *                startIndex = 0
     *                isCaseSensitive = false
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * fragment = { 'b', 'c' , 'd' }
     *                name = { 'a', 'b'}
     *                startIndex = 0
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param fragment
     *            the fragment to check
     * @param name
     *            the array to check
     * @param startIndex
     *            the starting index
     * @param isCaseSensitive
     *            check whether or not the equality should be case sensitive
     * @return true if the name contains the fragment at the starting index
     *         startIndex according to the value of isCaseSensitive, otherwise
     *         false.
     * @throws NullPointerException
     *             if fragment or name is null.
     */
    public static boolean fragmentEquals(final char[] fragment, final char[] name,
            final int startIndex, final boolean isCaseSensitive) {

        final int max = fragment.length;
        if (name.length < max + startIndex) {
            return false;
        }
        if (isCaseSensitive) {
            for (int i = max; --i >= 0;) {
                // assumes the prefix is not larger than the name
                if (fragment[i] != name[i + startIndex]) {
                    return false;
                }
            }
            return true;
        }
        for (int i = max; --i >= 0;) {
            // assumes the prefix is not larger than the name
            if (Character.toLowerCase(fragment[i]) != Character.toLowerCase(name[i
                    + startIndex])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Answers a hashcode for the array
     *
     * @param array
     *            the array for which a hashcode is required
     * @return the hashcode
     * @throws NullPointerException
     *             if array is null
     */
    public static int hashCode(final char[] array) {
        int hash = 0;
        int offset = 0;
        final int length = array.length;
        if (length < 16) {
            for (int i = length; i > 0; i--) {
                hash = hash * 37 + array[offset++];
            }
        } else {
            // only sample some characters
            final int skip = length / 8;
            for (int i = length; i > 0; i -= skip, offset += skip) {
                hash = hash * 39 + array[offset];
            }
        }
        return hash & 0x7FFFFFFF;
    }

    /**
     * Answers true if c is a whitespace according to the JLS (&#92;u000a,
     * &#92;u000c, &#92;u000d, &#92;u0009), otherwise false. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * c = ' '
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * c = ' \u3000'
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param c
     *            the character to check
     * @return true if c is a whitespace according to the JLS, otherwise false.
     */
    public static boolean isWhitespace(final char c) {
        switch (c) {
        case 10: /* \ u000a: LINE FEED */
        case 12: /* \ u000c: FORM FEED */
        case 13: /* \ u000d: CARRIAGE RETURN */
        case 32: /* \ u0020: SPACE */
        case 9: /* \ u0009: HORIZONTAL TABULATION */
            return true;
        default:
            return false;
        }
    }

    /**
     * Answers the first index in the array for which the corresponding
     * character is equal to toBeFound. Answers -1 if no occurrence of this
     * character is found. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' }
     *                result =&gt; 2
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'e'
     *                array = { ' a', 'b', 'c', 'd' }
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the character to search
     * @param array
     *            the array to be searched
     * @return the first index in the array for which the corresponding
     *         character is equal to toBeFound, -1 otherwise
     * @throws NullPointerException
     *             if array is null
     */
    public static int indexOf(final char toBeFound, final char[] array) {
        for (int i = 0; i < array.length; i++) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Answers the first index in the array for which the corresponding
     * character is equal to toBeFound starting the search at index start.
     * Answers -1 if no occurrence of this character is found. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' }
     *                start = 2
     *                result =&gt; 2
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' }
     *                start = 3
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'e'
     *                array = { ' a', 'b', 'c', 'd' }
     *                start = 1
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the character to search
     * @param array
     *            the array to be searched
     * @param start
     *            the starting index
     * @return the first index in the array for which the corresponding
     *         character is equal to toBeFound, -1 otherwise
     * @throws NullPointerException
     *             if array is null
     * @throws ArrayIndexOutOfBoundsException
     *             if start is lower than 0
     */
    public static int indexOf(final char toBeFound, final char[] array, final int start) {
        for (int i = start; i < array.length; i++) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Answers the last index in the array for which the corresponding character
     * is equal to toBeFound starting from the end of the array. Answers -1 if
     * no occurrence of this character is found. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' , 'c', 'e' }
     *                result =&gt; 4
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'e'
     *                array = { ' a', 'b', 'c', 'd' }
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the character to search
     * @param array
     *            the array to be searched
     * @return the last index in the array for which the corresponding character
     *         is equal to toBeFound starting from the end of the array, -1
     *         otherwise
     * @throws NullPointerException
     *             if array is null
     */
    public static int lastIndexOf(final char toBeFound, final char[] array) {
        for (int i = array.length; --i >= 0;) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Answers the last index in the array for which the corresponding character
     * is equal to toBeFound stopping at the index startIndex. Answers -1 if no
     * occurrence of this character is found. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' }
     *                startIndex = 2
     *                result =&gt; 2
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd', 'e' }
     *                startIndex = 3
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'e'
     *                array = { ' a', 'b', 'c', 'd' }
     *                startIndex = 0
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the character to search
     * @param array
     *            the array to be searched
     * @param startIndex
     *            the stopping index
     * @return the last index in the array for which the corresponding character
     *         is equal to toBeFound stopping at the index startIndex, -1
     *         otherwise
     * @throws NullPointerException
     *             if array is null
     * @throws ArrayIndexOutOfBoundsException
     *             if startIndex is lower than 0
     */
    public static int lastIndexOf(final char toBeFound, final char[] array,
            final int startIndex) {
        for (int i = array.length; --i >= startIndex;) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Answers the last index in the array for which the corresponding character
     * is equal to toBeFound starting from endIndex to startIndex. Answers -1 if
     * no occurrence of this character is found. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd' }
     *                startIndex = 2
     *                endIndex = 2
     *                result =&gt; 2
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { ' a', 'b', 'c', 'd', 'e' }
     *                startIndex = 3
     *                endIndex = 4
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'e'
     *                array = { ' a', 'b', 'c', 'd' }
     *                startIndex = 0
     *                endIndex = 3
     *                result =&gt; -1
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the character to search
     * @param array
     *            the array to be searched
     * @param startIndex
     *            the stopping index
     * @param endIndex
     *            the starting index
     * @return the last index in the array for which the corresponding character
     *         is equal to toBeFound starting from endIndex to startIndex, -1
     *         otherwise
     * @throws NullPointerException
     *             if array is null
     * @throws ArrayIndexOutOfBoundsException
     *             if endIndex is greater or equals to array length or starting
     *             is lower than 0
     */
    public static int lastIndexOf(final char toBeFound, final char[] array,
            final int startIndex, final int endIndex) {
        for (int i = endIndex; --i >= startIndex;) {
            if (toBeFound == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Answers the last portion of a name given a separator. <br>
     * <br>
     * For example,
     *
     * <pre>
     * lastSegment(&quot;java.lang.Object&quot;.toCharArray(),'.') --&gt; Object
     * </pre>
     *
     * @param array
     *            the array
     * @param separator
     *            the given separator
     * @return the last portion of a name given a separator
     * @throws NullPointerException
     *             if array is null
     */
    public static char[] lastSegment(final char[] array, final char separator) {
        final int pos = lastIndexOf(separator, array);
        if (pos < 0) {
            return array;
        }
        return subarray(array, pos + 1, array.length);
    }

    /**
     * Answers true if the pattern matches the given name, false otherwise. This
     * char[] pattern matching accepts wild-cards '*' and '?'.
     *
     * When not case sensitive, the pattern is assumed to already be lowercased,
     * the name will be lowercased character per character as comparing. If name
     * is null, the answer is false. If pattern is null, the answer is true if
     * name is not null. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * pattern = { '?', 'b', '*' }
     *                name = { 'a', 'b', 'c' , 'd' }
     *                isCaseSensitive = true
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * pattern = { '?', 'b', '?' }
     *                name = { 'a', 'b', 'c' , 'd' }
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * pattern = { 'b', '*' }
     *                name = { 'a', 'b', 'c' , 'd' }
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param pattern
     *            the given pattern
     * @param name
     *            the given name
     * @param isCaseSensitive
     *            flag to know whether or not the matching should be case
     *            sensitive
     * @return true if the pattern matches the given name, false otherwise
     */
    public static boolean match(final char[] pattern, final char[] name,
            final boolean isCaseSensitive) {

        if (name == null) {
            return false; // null name cannot match
        }
        if (pattern == null) {
            return true; // null pattern is equivalent to '*'
        }

        return match(pattern, 0, pattern.length, name, 0, name.length, isCaseSensitive);
    }

    /**
     * Answers true if the a sub-pattern matches the subpart of the given name,
     * false otherwise. char[] pattern matching, accepting wild-cards '*' and
     * '?'. Can match only subset of name/pattern. end positions are
     * non-inclusive. The subpattern is defined by the patternStart and
     * pattternEnd positions. When not case sensitive, the pattern is assumed to
     * already be lowercased, the name will be lowercased character per
     * character as comparing. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * pattern = { '?', 'b', '*' }
     *                patternStart = 1
     *                patternEnd = 3
     *                name = { 'a', 'b', 'c' , 'd' }
     *                nameStart = 1
     *                nameEnd = 4
     *                isCaseSensitive = true
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * pattern = { '?', 'b', '*' }
     *                patternStart = 1
     *                patternEnd = 2
     *                name = { 'a', 'b', 'c' , 'd' }
     *                nameStart = 1
     *                nameEnd = 2
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param pattern
     *            the given pattern
     * @param patternStart
     *            the given pattern start
     * @param patternEnd
     *            the given pattern end
     * @param name
     *            the given name
     * @param nameStart
     *            the given name start
     * @param nameEnd
     *            the given name end
     * @param isCaseSensitive
     *            flag to know if the matching should be case sensitive
     * @return true if the a sub-pattern matches the subpart of the given name,
     *         false otherwise
     */
    public static boolean match(final char[] pattern, final int patternStart,
            final int patternEnd0, final char[] name, final int nameStart,
            final int nameEnd0, final boolean isCaseSensitive) {

        if (name == null) {
            return false; // null name cannot match
        }
        if (pattern == null) {
            return true; // null pattern is equivalent to '*'
        }
        int iPattern = patternStart;
        int iName = nameStart;
        int patternEnd = patternEnd0;
        int nameEnd = nameEnd0;
        if (patternEnd < 0) {
            patternEnd = pattern.length;
        }
        if (nameEnd < 0) {
            nameEnd = name.length;
        }

        /* check first segment */
        char patternChar = 0;
        while (iPattern < patternEnd && (patternChar = pattern[iPattern]) != '*') {
            if (iName == nameEnd) {
                return false;
            }
            if (patternChar != (isCaseSensitive ? name[iName] : Character
                    .toLowerCase(name[iName])) && patternChar != '?') {
                return false;
            }
            iName++;
            iPattern++;
        }
        /* check sequence of star+segment */
        int segmentStart;
        if (patternChar == '*') {
            segmentStart = ++iPattern; // skip star
        } else {
            segmentStart = 0; // force iName check
        }
        int prefixStart = iName;
        checkSegment:
        while (iName < nameEnd) {
            if (iPattern == patternEnd) {
                iPattern = segmentStart; // mismatch - restart current
                // segment
                iName = ++prefixStart;
                continue checkSegment;
            }
            /* segment is ending */
            if ((patternChar = pattern[iPattern]) == '*') {
                segmentStart = ++iPattern; // skip start
                if (segmentStart == patternEnd) {
                    return true;
                }
                prefixStart = iName;
                continue checkSegment;
            }
            /* check current name character */
            if ((isCaseSensitive ? name[iName] : Character.toLowerCase(name[iName])) != patternChar
                    && patternChar != '?') {
                iPattern = segmentStart; // mismatch - restart current
                // segment
                iName = ++prefixStart;
                continue checkSegment;
            }
            iName++;
            iPattern++;
        }

        return segmentStart == patternEnd || iName == nameEnd && iPattern == patternEnd
                || iPattern == patternEnd - 1 && pattern[iPattern] == '*';
    }

    /**
     * Answers true if the pattern matches the filepath using the pathSepatator,
     * false otherwise.
     *
     * Path char[] pattern matching, accepting wild-cards '**', '*' and '?'
     * (using Ant directory tasks conventions, also see
     * "http://jakarta.apache.org/ant/manual/dirtasks.html#defaultexcludes").
     * Path pattern matching is enhancing regular pattern matching in supporting
     * extra rule where '**' represent any folder combination. Special rule: -
     * foo\ is equivalent to foo\** When not case sensitive, the pattern is
     * assumed to already be lowercased, the name will be lowercased character
     * per character as comparing.
     *
     * @param pattern
     *            the given pattern
     * @param filepath
     *            the given path
     * @param isCaseSensitive
     *            to find out whether or not the matching should be case
     *            sensitive
     * @param pathSeparator
     *            the given path separator
     * @return true if the pattern matches the filepath using the pathSepatator,
     *         false otherwise
     */
    public static boolean pathMatch(final char[] pattern, final char[] filepath,
            final boolean isCaseSensitive, final char pathSeparator) {

        if (filepath == null) {
            return false; // null name cannot match
        }
        if (pattern == null) {
            return true; // null pattern is equivalent to '*'
        }

        // offsets inside pattern
        int pSegmentStart = pattern[0] == pathSeparator ? 1 : 0;
        final int pLength = pattern.length;
        int pSegmentEnd = CharOperation
                .indexOf(pathSeparator, pattern, pSegmentStart + 1);
        if (pSegmentEnd < 0) {
            pSegmentEnd = pLength;
        }

        // special case: pattern foo\ is equivalent to foo\**
        final boolean freeTrailingDoubleStar = pattern[pLength - 1] == pathSeparator;

        // offsets inside filepath
        int fSegmentStart;
        final int fLength = filepath.length;
        if (filepath[0] != pathSeparator) {
            fSegmentStart = 0;
        } else {
            fSegmentStart = 1;
        }
        if (fSegmentStart != pSegmentStart) {
            return false; // both must start with a separator or none.
        }
        int fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath,
                fSegmentStart + 1);
        if (fSegmentEnd < 0) {
            fSegmentEnd = fLength;
        }

        // first segments
        while (pSegmentStart < pLength
                && !(pSegmentEnd == pLength && freeTrailingDoubleStar || pSegmentEnd == pSegmentStart + 2
                        && pattern[pSegmentStart] == '*'
                        && pattern[pSegmentStart + 1] == '*')) {

            if (fSegmentStart >= fLength) {
                return false;
            }
            if (!CharOperation.match(pattern, pSegmentStart, pSegmentEnd, filepath,
                    fSegmentStart, fSegmentEnd, isCaseSensitive)) {
                return false;
            }

            // jump to next segment
            pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                    pSegmentStart = pSegmentEnd + 1);
            // skip separator
            if (pSegmentEnd < 0) {
                pSegmentEnd = pLength;
            }

            fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath,
                    fSegmentStart = fSegmentEnd + 1);
            // skip separator
            if (fSegmentEnd < 0) {
                fSegmentEnd = fLength;
            }
        }

        /* check sequence of doubleStar+segment */
        int pSegmentRestart;
        if (pSegmentStart >= pLength && freeTrailingDoubleStar
                || pSegmentEnd == pSegmentStart + 2 && pattern[pSegmentStart] == '*'
                && pattern[pSegmentStart + 1] == '*') {
            pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                    pSegmentStart = pSegmentEnd + 1);
            // skip separator
            if (pSegmentEnd < 0) {
                pSegmentEnd = pLength;
            }
            pSegmentRestart = pSegmentStart;
        } else {
            if (pSegmentStart >= pLength) {
                return fSegmentStart >= fLength; // true if filepath is done
            }
            // too.
            pSegmentRestart = 0; // force fSegmentStart check
        }
        int fSegmentRestart = fSegmentStart;
        checkSegment:
        while (fSegmentStart < fLength) {

            if (pSegmentStart >= pLength) {
                if (freeTrailingDoubleStar) {
                    return true;
                }
                // mismatch - restart current path segment
                pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                        pSegmentStart = pSegmentRestart);
                if (pSegmentEnd < 0) {
                    pSegmentEnd = pLength;
                }

                fSegmentRestart = CharOperation.indexOf(pathSeparator, filepath,
                        fSegmentRestart + 1);
                // skip separator
                if (fSegmentRestart < 0) {
                    fSegmentRestart = fLength;
                } else {
                    fSegmentRestart++;
                }
                fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath,
                        fSegmentStart = fSegmentRestart);
                if (fSegmentEnd < 0) {
                    fSegmentEnd = fLength;
                }
                continue checkSegment;
            }

            /* path segment is ending */
            if (pSegmentEnd == pSegmentStart + 2 && pattern[pSegmentStart] == '*'
                    && pattern[pSegmentStart + 1] == '*') {
                pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                        pSegmentStart = pSegmentEnd + 1);
                // skip separator
                if (pSegmentEnd < 0) {
                    pSegmentEnd = pLength;
                }
                pSegmentRestart = pSegmentStart;
                fSegmentRestart = fSegmentStart;
                if (pSegmentStart >= pLength) {
                    return true;
                }
                continue checkSegment;
            }
            /* chech current path segment */
            if (!CharOperation.match(pattern, pSegmentStart, pSegmentEnd, filepath,
                    fSegmentStart, fSegmentEnd, isCaseSensitive)) {
                // mismatch - restart current path segment
                pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                        pSegmentStart = pSegmentRestart);
                if (pSegmentEnd < 0) {
                    pSegmentEnd = pLength;
                }

                fSegmentRestart = CharOperation.indexOf(pathSeparator, filepath,
                        fSegmentRestart + 1);
                // skip separator
                if (fSegmentRestart < 0) {
                    fSegmentRestart = fLength;
                } else {
                    fSegmentRestart++;
                }
                fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath,
                        fSegmentStart = fSegmentRestart);
                if (fSegmentEnd < 0) {
                    fSegmentEnd = fLength;
                }
                continue checkSegment;
            }
            // jump to next segment
            pSegmentEnd = CharOperation.indexOf(pathSeparator, pattern,
                    pSegmentStart = pSegmentEnd + 1);
            // skip separator
            if (pSegmentEnd < 0) {
                pSegmentEnd = pLength;
            }

            fSegmentEnd = CharOperation.indexOf(pathSeparator, filepath,
                    fSegmentStart = fSegmentEnd + 1);
            // skip separator
            if (fSegmentEnd < 0) {
                fSegmentEnd = fLength;
            }
        }

        return pSegmentRestart >= pSegmentEnd || fSegmentStart >= fLength
                && pSegmentStart >= pLength || pSegmentStart == pLength - 2
                && pattern[pSegmentStart] == '*' && pattern[pSegmentStart + 1] == '*'
                || pSegmentStart == pLength && freeTrailingDoubleStar;
    }

    /**
     * Answers the number of occurrences of the given character in the given
     * array, 0 if any. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'b'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; 3
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; 0
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the given character
     * @param array
     *            the given array
     * @return the number of occurrences of the given character in the given
     *         array, 0 if any
     * @throws NullPointerException
     *             if array is null
     */
    public static int occurrencesOf(final char toBeFound, final char[] array) {
        return occurrencesOf(toBeFound, array, 0);
    }

    /**
     * Answers the number of occurrences of the given character in the given
     * array starting at the given index, 0 if any. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * toBeFound = 'b'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                start = 2
     *                result =&gt; 2
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * toBeFound = 'c'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                start = 0
     *                result =&gt; 0
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param toBeFound
     *            the given character
     * @param array
     *            the given array
     * @param start
     *            the given index
     * @return the number of occurrences of the given character in the given
     *         array, 0 if any
     * @throws NullPointerException
     *             if array is null
     * @throws ArrayIndexOutOfBoundsException
     *             if start is lower than 0
     */
    public static int occurrencesOf(final char toBeFound, final char[] array,
            final int start) {
        int count = 0;
        for (int i = start; i < array.length; i++) {
            if (toBeFound == array[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Answers true if the given name starts with the given prefix, false
     * otherwise. The comparison is case sensitive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * prefix = { 'a' , 'b' }
     *                name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * prefix = { 'a' , 'c' }
     *                name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param prefix
     *            the given prefix
     * @param name
     *            the given name
     * @return true if the given name starts with the given prefix, false
     *         otherwise
     * @throws NullPointerException
     *             if the given name is null or if the given prefix is null
     */
    public static boolean prefixEquals(final char[] prefix, final char[] name) {

        final int max = prefix.length;
        if (name.length < max) {
            return false;
        }
        for (int i = max; --i >= 0;) {
            // assumes the prefix is not larger than the name
            if (prefix[i] != name[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Answers true if the given name starts with the given prefix, false
     * otherwise. isCaseSensitive is used to find out whether or not the
     * comparison should be case sensitive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * prefix = { 'a' , 'B' }
     *                name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                isCaseSensitive = false
     *                result =&gt; true
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * prefix = { 'a' , 'B' }
     *                name = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                isCaseSensitive = true
     *                result =&gt; false
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param prefix
     *            the given prefix
     * @param name
     *            the given name
     * @param isCaseSensitive
     *            to find out whether or not the comparison should be case
     *            sensitive
     * @return true if the given name starts with the given prefix, false
     *         otherwise
     * @throws NullPointerException
     *             if the given name is null or if the given prefix is null
     */
    public static boolean prefixEquals(final char[] prefix, final char[] name,
            final boolean isCaseSensitive) {

        final int max = prefix.length;
        if (name.length < max) {
            return false;
        }
        if (isCaseSensitive) {
            for (int i = max; --i >= 0;) {
                // assumes the prefix is not larger than the name
                if (prefix[i] != name[i]) {
                    return false;
                }
            }
            return true;
        }

        for (int i = max; --i >= 0;) {
            // assumes the prefix is not larger than the name
            if (Character.toLowerCase(prefix[i]) != Character.toLowerCase(name[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Replace all occurrence of the character to be replaced with the
     * remplacement character in the given array. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                toBeReplaced = 'b'
     *                replacementChar = 'a'
     *                result =&gt; No returned value, but array is now equals to { 'a' , 'a', 'a', 'a', 'a', 'a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                toBeReplaced = 'c'
     *                replacementChar = 'a'
     *                result =&gt; No returned value, but array is now equals to { 'a' , 'b', 'b', 'a', 'b', 'a' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param toBeReplaced
     *            the character to be replaced
     * @param replacementChar
     *            the replacement character
     * @throws NullPointerException
     *             if the given array is null
     */
    public static void replace(final char[] array, final char toBeReplaced,
            final char replacementChar) {
        if (toBeReplaced != replacementChar) {
            for (int i = 0, max = array.length; i < max; i++) {
                if (array[i] == toBeReplaced) {
                    array[i] = replacementChar;
                }
            }
        }
    }

    /**
     * Answers a new array of characters with substitutions. No side-effect is
     * operated on the original array, in case no substitution happened, then
     * the result is the same as the original one. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                toBeReplaced = { 'b' }
     *                replacementChar = { 'a', 'a' }
     *                result =&gt; { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                toBeReplaced = { 'c' }
     *                replacementChar = { 'a' }
     *                result =&gt; { 'a' , 'b', 'b', 'a', 'b', 'a' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param toBeReplaced
     *            characters to be replaced
     * @param replacementChars
     *            the replacement characters
     * @return a new array of characters with substitutions or the given array
     *         if none
     * @throws NullPointerException
     *             if the given array is null
     */
    public static char[] replace(final char[] array, final char[] toBeReplaced,
            final char[] replacementChars) {

        final int max = array.length;
        final int replacedLength = toBeReplaced.length;
        final int replacementLength = replacementChars.length;

        int[] starts = new int[5];
        int occurrenceCount = 0;

        if (!equals(toBeReplaced, replacementChars)) {

            next:
            for (int i = 0; i < max; i++) {
                int j = 0;
                while (j < replacedLength) {
                    if (i + j == max) {
                        continue next;
                    }
                    if (array[i + j] != toBeReplaced[j++]) {
                        continue next;
                    }
                }
                if (occurrenceCount == starts.length) {
                    System.arraycopy(starts, 0, starts = new int[occurrenceCount * 2], 0,
                            occurrenceCount);
                }
                starts[occurrenceCount++] = i;
            }
        }
        if (occurrenceCount == 0) {
            return array;
        }
        final char[] result = new char[max + occurrenceCount
                * (replacementLength - replacedLength)];
        int inStart = 0, outStart = 0;
        for (int i = 0; i < occurrenceCount; i++) {
            final int offset = starts[i] - inStart;
            System.arraycopy(array, inStart, result, outStart, offset);
            inStart += offset;
            outStart += offset;
            System.arraycopy(replacementChars, 0, result, outStart, replacementLength);
            inStart += replacedLength;
            outStart += replacementLength;
        }
        System.arraycopy(array, inStart, result, outStart, max - inStart);
        return result;
    }

    /**
     * Return a new array which is the split of the given array using the given
     * divider and triming each subarray to remove whitespaces equals to ' '. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * divider = 'b'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; { { 'a' }, {  }, { 'a' }, { 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * divider = 'c'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; { { 'a', 'b', 'b', 'a', 'b', 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * divider = 'b'
     *                array = { 'a' , ' ', 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; { { 'a' }, {  }, { 'a' }, { 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * divider = 'c'
     *                array = { ' ', ' ', 'a' , 'b', 'b', 'a', 'b', 'a', ' ' }
     *                result =&gt; { { 'a', 'b', 'b', 'a', 'b', 'a' } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param divider
     *            the given divider
     * @param array
     *            the given array
     * @return a new array which is the split of the given array using the given
     *         divider and triming each subarray to remove whitespaces equals to
     *         ' '
     */
    @SuppressWarnings("null")
    public static char[][] splitAndTrimOn(final char divider, final char[] array) {
        final int length = array == null ? 0 : array.length;
        if (length == 0) {
            return NO_CHAR_CHAR;
        }

        int wordCount = 1;
        for (int i = 0; i < length; i++) {
            if (array[i] == divider) {
                wordCount++;
            }
        }
        final char[][] split = new char[wordCount][];
        int last = 0, currentWord = 0;
        for (int i = 0; i < length; i++) {
            if (array[i] == divider) {
                int start = last, end = i - 1;
                while (start < i && array[start] == ' ') {
                    start++;
                }
                while (end > start && array[end] == ' ') {
                    end--;
                }
                split[currentWord] = new char[end - start + 1];
                System.arraycopy(array, start, split[currentWord++], 0, end - start + 1);
                last = i + 1;
            }
        }
        int start = last, end = length - 1;
        while (start < length && array[start] == ' ') {
            start++;
        }
        while (end > start && array[end] == ' ') {
            end--;
        }
        split[currentWord] = new char[end - start + 1];
        System.arraycopy(array, start, split[currentWord++], 0, end - start + 1);
        return split;
    }

    /**
     * Return a new array which is the split of the given array using the given
     * divider. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * divider = 'b'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; { { 'a' }, {  }, { 'a' }, { 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * divider = 'c'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                result =&gt; { { 'a', 'b', 'b', 'a', 'b', 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * divider = 'c'
     *                array = { ' ', ' ', 'a' , 'b', 'b', 'a', 'b', 'a', ' ' }
     *                result =&gt; { { ' ', 'a', 'b', 'b', 'a', 'b', 'a', ' ' } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param divider
     *            the given divider
     * @param array
     *            the given array
     * @return a new array which is the split of the given array using the given
     *         divider
     */
    public static char[][] splitOn(final char divider, final char[] array) {
        final int length = array == null ? 0 : array.length;
        return splitOn(divider, array, 0, length);
    }

    /**
     * Return a new array which is the split of the given array using the given
     * divider. The given end is exclusive and the given start is inclusive. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * divider = 'b'
     *                array = { 'a' , 'b', 'b', 'a', 'b', 'a' }
     *                start = 2
     *                end = 5
     *                result =&gt; { {  }, { 'a' }, {  } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param divider
     *            the given divider
     * @param array
     *            the given array
     * @param start
     *            the given starting index
     * @param end
     *            the given ending index
     * @return a new array which is the split of the given array using the given
     *         divider
     * @throws ArrayIndexOutOfBoundsException
     *             if start is lower than 0 or end is greater than the array
     *             length
     */
    @SuppressWarnings("null")
    public static char[][] splitOn(final char divider, final char[] array,
            final int start, final int end) {
        final int length = array == null ? 0 : array.length;
        if (length == 0 || start > end) {
            return NO_CHAR_CHAR;
        }

        int wordCount = 1;
        for (int i = start; i < end; i++) {
            if (array[i] == divider) {
                wordCount++;
            }
        }
        final char[][] split = new char[wordCount][];
        int last = start, currentWord = 0;
        for (int i = start; i < end; i++) {
            if (array[i] == divider) {
                split[currentWord] = new char[i - last];
                System.arraycopy(array, last, split[currentWord++], 0, i - last);
                last = i + 1;
            }
        }
        split[currentWord] = new char[end - last];
        System.arraycopy(array, last, split[currentWord], 0, end - last);
        return split;
    }

    /**
     * Answers a new array which is a copy of the given array starting at the
     * given start and ending at the given end. The given start is inclusive and
     * the given end is exclusive. Answers null if start is greater than end, if
     * start is lower than 0 or if end is greater than the length of the given
     * array. If end equals -1, it is converted to the array length. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { { 'a' } , { 'b' } }
     *                start = 0
     *                end = 1
     *                result =&gt; { { 'a' } }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { { 'a' } , { 'b' } }
     *                start = 0
     *                end = -1
     *                result =&gt; { { 'a' }, { 'b' } }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param start
     *            the given starting index
     * @param end
     *            the given ending index
     * @return a new array which is a copy of the given array starting at the
     *         given start and ending at the given end
     * @throws NullPointerException
     *             if the given array is null
     */
    public static char[][] subarray(final char[][] array, final int start, final int end0) {
        int end = end0;
        if (end == -1) {
            end = array.length;
        }
        if (start > end) {
            return null;
        }
        if (start < 0) {
            return null;
        }
        if (end > array.length) {
            return null;
        }

        final char[][] result = new char[end - start][];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    /**
     * Answers a new array which is a copy of the given array starting at the
     * given start and ending at the given end. The given start is inclusive and
     * the given end is exclusive. Answers null if start is greater than end, if
     * start is lower than 0 or if end is greater than the length of the given
     * array. If end equals -1, it is converted to the array length. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { 'a' , 'b' }
     *                start = 0
     *                end = 1
     *                result =&gt; { 'a' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'a', 'b' }
     *                start = 0
     *                end = -1
     *                result =&gt; { 'a' , 'b' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @param start
     *            the given starting index
     * @param end
     *            the given ending index
     * @return a new array which is a copy of the given array starting at the
     *         given start and ending at the given end
     * @throws NullPointerException
     *             if the given array is null
     */
    public static char[] subarray(final char[] array, final int start, final int end0) {
        int end = end0;
        if (end == -1) {
            end = array.length;
        }
        if (start > end) {
            return null;
        }
        if (start < 0) {
            return null;
        }
        if (end > array.length) {
            return null;
        }

        final char[] result = new char[end - start];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    /**
     * Answers the result of a char[] conversion to lowercase. Answers null if
     * the given chars array is null. <br>
     * NOTE: If no conversion was necessary, then answers back the argument one. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * chars = { 'a' , 'b' }
     *                result =&gt; { 'a' , 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'A', 'b' }
     *                result =&gt; { 'a' , 'b' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param chars
     *            the chars to convert
     * @return the result of a char[] conversion to lowercase
     */
    public static char[] toLowerCase(final char[] chars) {
        if (chars == null) {
            return null;
        }
        final int length = chars.length;
        char[] lowerChars = null;
        for (int i = 0; i < length; i++) {
            final char c = chars[i];
            final char lc = Character.toLowerCase(c);
            if (c != lc || lowerChars != null) {
                if (lowerChars == null) {
                    System.arraycopy(chars, 0, lowerChars = new char[length], 0, i);
                }
                lowerChars[i] = lc;
            }
        }
        return lowerChars == null ? chars : lowerChars;
    }

    /**
     * Answers a new array removing leading and trailing spaces (' '). Answers
     * the given array if there is no space characters to remove. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * chars = { ' ', 'a' , 'b', ' ',  ' ' }
     *                result =&gt; { 'a' , 'b' }
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { 'A', 'b' }
     *                result =&gt; { 'A' , 'b' }
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param chars
     *            the given array
     * @return a new array removing leading and trailing spaces (' ')
     */
    public static char[] trim(final char[] chars) {

        if (chars == null) {
            return null;
        }

        int start = 0;
        final int length = chars.length;
        int end = length - 1;
        while (start < length && chars[start] == ' ') {
            start++;
        }
        while (end > start && chars[end] == ' ') {
            end--;
        }
        if (start != 0 || end != length - 1) {
            return subarray(chars, start, end + 1);
        }
        return chars;
    }

    /**
     * Answers a string which is the concatenation of the given array using the
     * '.' as a separator. <br>
     * <br>
     * For example:
     * <ol>
     * <li>
     *
     * <pre>
     * array = { { 'a' } , { 'b' } }
     *                result =&gt; &quot;a.b&quot;
     * </pre>
     *
     * </li>
     * <li>
     *
     * <pre>
     * array = { { ' ',  'a' } , { 'b' } }
     *                result =&gt; &quot; a.b&quot;
     * </pre>
     *
     * </li>
     * </ol>
     *
     * @param array
     *            the given array
     * @return a string which is the concatenation of the given array using the
     *         '.' as a separator
     */
    public static String toString(final char[][] array) {
        final char[] result = concatWith(array, '.');
        return new String(result);
    }

    /**
     * Answers an array of strings from the given array of char array.
     *
     * @param array
     *            the given array
     * @return an array of strings
     *
     */
    public static String[] toStrings(final char[][] array) {
        final int length = array.length;
        final String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = new String(array[i]);
        }
        return result;
    }
}

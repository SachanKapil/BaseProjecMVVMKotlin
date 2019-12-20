package com.baseprojectmvvmkotlin.util

import android.animation.LayoutTransition
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ReadMoreOption private constructor(builder: Builder) {

    companion object {
        const val TYPE_LINE = 1
        const val TYPE_CHARACTER = 2
    }

    // optional
    private var textLength: Int = 0
    private var textLengthType: Int = 0
    private var moreLabel: String = ""
    private var lessLabel: String = ""
    private var moreLabelColor: Int = 0
    private var lessLabelColor: Int = 0
    private var labelUnderLine: Boolean = false
    private var expandAnimation: Boolean = false

    init {
        this.textLength = builder.textLength
        this.textLengthType = builder.textLengthType
        this.moreLabel = builder.moreLabel
        this.lessLabel = builder.lessLabel
        this.moreLabelColor = builder.moreLabelColor
        this.lessLabelColor = builder.lessLabelColor
        this.labelUnderLine = builder.labelUnderLine
        this.expandAnimation = builder.expandAnimation
    }

//    private void findLinks(TextView tvPostContent, CharSequence postText) {
//        if (TextUtils.isEmpty(postText))
//            return;
//        try {
//            Matcher m = TextFormatter.URL_PATTERN.matcher(postText);
//            while (m.find()) {
//                addLink(tvPostContent, m.group());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void addLink(TextView tvPostContent, String linkText) {
//        Link link = new Link(linkText)
//                .setTextColor(Color.parseColor("#00A3A1"))
//                .setTextColorOfHighlightedLink(Color.parseColor("#009A44"))
//                .setHighlightAlpha(.4f)
//                .setUnderlined(true)
//                .setBold(true)
//                .setOnClickListener(clickedText -> {
//                    clickedText = clickedText.trim();
//                    if (!clickedText.startsWith("https://") && !clickedText.startsWith("http://")) {
//                        clickedText = "http://" + clickedText;
//                    }
//                    Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedText));
//                    App.getAppContext().startActivity(openUrlIntent);
//                });
//
//        LinkBuilder.on(tvPostContent)
//                .addLink(link)
//                .build();
//    }

    fun addReadMoreTo(textView: TextView, text: CharSequence) {
        if (textLengthType == TYPE_CHARACTER) {
            if (text.length <= textLength) {
                textView.text = text
                // findLinks(textView, text);
                return
            }
        } else {
            // If TYPE_LINE
            textView.setLines(textLength)
            textView.text = text
            // findLinks(textView, text);
        }

        textView.post(Runnable {
            var textLengthNew = textLength

            if (textLengthType == TYPE_LINE) {


                if (textView.layout.lineCount <= textLength) {
                    textView.text = text
                    // findLinks(textView, text);
                    return@Runnable
                }

                val lp = textView.layoutParams as ViewGroup.MarginLayoutParams

                val subString = text.toString().substring(
                    textView.layout.getLineStart(0),
                    textView.layout.getLineEnd(textLength - 1)
                )
                textLengthNew = subString.length - (moreLabel.length + 4 + lp.rightMargin / 6)
            }

            val spannableStringBuilder = SpannableStringBuilder(text.subSequence(0, textLengthNew))
                .append("... ")
                .append(moreLabel)

            val ss = SpannableString.valueOf(spannableStringBuilder)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    addReadLess(textView, text)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = labelUnderLine
                    ds.color = moreLabelColor
                    ds.typeface = Typeface.DEFAULT_BOLD
                }
            }
            ss.setSpan(
                clickableSpan,
                ss.length - moreLabel.length,
                ss.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && expandAnimation) {
                val layoutTransition = LayoutTransition()
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                (textView.parent as ViewGroup).layoutTransition = layoutTransition
            }

            textView.text = ss
//            findLinks(textView, ss);
            textView.movementMethod = LinkMovementMethod.getInstance()
        })
    }

    private fun addReadLess(textView: TextView, text: CharSequence) {
        textView.maxLines = Integer.MAX_VALUE

        val spannableStringBuilder = SpannableStringBuilder(text)
            .append(" ")
            .append(lessLabel)

        val ss = SpannableString.valueOf(spannableStringBuilder)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Handler().post { addReadMoreTo(textView, text) }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = labelUnderLine
                ds.color = lessLabelColor
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        ss.setSpan(
            clickableSpan,
            ss.length - lessLabel.length,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = ss
        //        findLinks(textView, ss);
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    class Builder constructor() {
        // optional
        var textLength = 100
        var textLengthType = TYPE_CHARACTER
        var moreLabel = "read more"
        var lessLabel = "read less"
        var moreLabelColor = Color.parseColor("#ff00ff")
        var lessLabelColor = Color.parseColor("#ff00ff")
        var labelUnderLine = false
        var expandAnimation = false

        /**
         * @param length         can be no. of line OR no. of characters - default is 100 character
         * @param textLengthType ReadMoreOption.TYPE_LINE for no. of line OR
         * ReadMoreOption.TYPE_CHARACTER for no. of character
         * - default is ReadMoreOption.TYPE_CHARACTER
         * @return Builder obj
         */

        fun textLength(length: Int, textLengthType: Int): Builder {
            this.textLength = length
            this.textLengthType = textLengthType
            return this
        }

        fun moreLabel(moreLabel: String): Builder {
            this.moreLabel = moreLabel
            return this
        }

        fun lessLabel(lessLabel: String): Builder {
            this.lessLabel = lessLabel
            return this
        }

        fun moreLabelColor(moreLabelColor: Int): Builder {
            this.moreLabelColor = moreLabelColor
            return this
        }

        fun lessLabelColor(lessLabelColor: Int): Builder {
            this.lessLabelColor = lessLabelColor
            return this
        }

        fun labelUnderLine(labelUnderLine: Boolean): Builder {
            this.labelUnderLine = labelUnderLine
            return this
        }

        /**
         * @param expandAnimation either true to enable animation on expand or false to disable animation
         * - default is false
         * @return Builder obj
         */
        fun expandAnimation(expandAnimation: Boolean): Builder {
            this.expandAnimation = expandAnimation
            return this
        }

        fun build(): ReadMoreOption {
            return ReadMoreOption(this)
        }
    }
}
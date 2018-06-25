package com.clloret.days.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.clloret.days.R;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem.Builder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;
import java.util.Calendar;

public class AboutActivity extends MaterialAboutActivity {

  public static final String AUTHOR_EMAIL = "clloretplayan@gmail.com";
  public static final String AUTHOR_NAME = "Carlos Lloret Playán";
  public static final String GITHUB_URL = "https://github.com/clloret/days";
  public static final String I_GIORNI_LUDOVICO_EINAUDI = "I Giorni (The Days) - Ludovico Einaudi";
  public static final String I_GIORNI_LUDOVICO_EINAUDI_YOUTUBE = "https://www.youtube.com/watch?v=Uffjii1hXzU";

  @NonNull
  @Override
  protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {

    return new MaterialAboutList.Builder()
        .addCard(buildApp(context))
        .addCard(buildAuthor(context))
        .addCard(buildSong(context))
        .addCard(buildLicense(context))
        .build();
  }

  @Nullable
  @Override
  protected CharSequence getActivityTitle() {

    return getString(R.string.title_activity_about);
  }

  private MaterialAboutCard buildApp(Context context) {

    MaterialAboutTitleItem titleItem = ConvenienceBuilder
        .createAppTitleItem(context);

    Drawable drawableInfo = ContextCompat.getDrawable(context, R.drawable.ic_info_24dp);
    drawableInfo.setTint(getColor(R.color.secondary_text));
    MaterialAboutActionItem versionItem = ConvenienceBuilder.createVersionActionItem(context,
        drawableInfo, getString(R.string.about_version), false);

    MaterialAboutCard.Builder cardBuilder = new MaterialAboutCard.Builder();

    return cardBuilder
        .addItem(titleItem)
        .addItem(versionItem)
        .build();
  }

  private MaterialAboutCard buildAuthor(Context context) {

    MaterialAboutActionItem nameItem = new Builder()
        .text(AUTHOR_NAME)
        .subText(R.string.about_country)
        .icon(R.drawable.ic_person_24dp)
        .build();

    Drawable drawableGithub = ContextCompat.getDrawable(context, R.drawable.ic_github_24dp);
    MaterialAboutActionItem githubItem = ConvenienceBuilder
        .createWebsiteActionItem(context, drawableGithub,
            getString(R.string.about_view_source_code), false, Uri.parse(GITHUB_URL));

    Drawable drawableEmail = ContextCompat.getDrawable(context, R.drawable.ic_email_24dp);
    MaterialAboutActionItem emailItem = ConvenienceBuilder
        .createEmailItem(context, drawableEmail, getString(R.string.about_send_email), true,
            AUTHOR_EMAIL, getString(R.string.about_my_question));

    MaterialAboutCard.Builder cardBuilder = new MaterialAboutCard.Builder();

    return cardBuilder
        .title(R.string.about_author)
        .addItem(nameItem)
        .addItem(githubItem)
        .addItem(emailItem)
        .build();
  }

  private MaterialAboutCard buildSong(Context context) {

    Drawable drawableYoutube = ContextCompat.getDrawable(context, R.drawable.ic_youtube_24dp);
    MaterialAboutActionItem youtubeItem = ConvenienceBuilder
        .createWebsiteActionItem(context, drawableYoutube, I_GIORNI_LUDOVICO_EINAUDI, false,
            Uri.parse(I_GIORNI_LUDOVICO_EINAUDI_YOUTUBE))
        .setSubTextRes(R.string.about_love_this_piano_song);

    MaterialAboutCard.Builder cardBuilder = new MaterialAboutCard.Builder();

    return cardBuilder
        .title(R.string.about_inspiration_song)
        .addItem(youtubeItem).build();
  }

  private MaterialAboutCard buildLicense(Context context) {

    Drawable drawableCopyright = ContextCompat.getDrawable(context, R.drawable.ic_copyright_24dp);
    Calendar calendar = Calendar.getInstance();
    String year = String.valueOf(calendar.get(Calendar.YEAR));
    return ConvenienceBuilder
        .createLicenseCard(context, drawableCopyright, getString(R.string.about_license), year,
            AUTHOR_NAME, OpenSourceLicense.GNU_GPL_3);
  }

}

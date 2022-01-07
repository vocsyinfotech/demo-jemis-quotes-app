package com.example.learninlife;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.varunest.sparkbutton.SparkButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Quotes_list_adapter extends RecyclerView.Adapter<Quotes_list_adapter.MyClass> {
    Quotes_screen_activity q_activity;
    ArrayList<Quotes_list> quotes_lists;
    Quote_interface anInterface;
    ArrayList<Integer> save_al = new ArrayList();
    My_database database;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public Quotes_list_adapter(Quotes_screen_activity quotes_screen_activity, ArrayList<Quotes_list> quotes_lists) {
        this.quotes_lists = quotes_lists;
        this.q_activity = quotes_screen_activity;
        anInterface = (Quote_interface) q_activity;
        prefs = PreferenceManager.getDefaultSharedPreferences(q_activity);
        editor = prefs.edit();
        editor.apply();
        database = new My_database(q_activity);
        database.openDataBase();
    }

    @Override
    public MyClass onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(q_activity).inflate(R.layout.quotes_item, parent, false);
        MyClass m = new MyClass(view);
        return m;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull Quotes_list_adapter.MyClass holder, int position) {
        Typeface typeface = ResourcesCompat.getFont(q_activity, R.font.font);
        holder.tv_content.setTypeface(typeface);

        holder.tv_content.setText(quotes_lists.get(position).getContent());

        int fav = database.get_like_quotes(quotes_lists.get(position).getQuote_id());
        if (fav == 0) {
            holder.like_bt.setChecked(false);
            holder.tv_like.setText("Like");
        } else {
            holder.like_bt.setChecked(true);
            holder.tv_like.setText("Liked");
        }

        holder.cardView.setCardBackgroundColor(R.color.white);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.cardView.setCardBackgroundColor(getRandomColorCode(quotes_lists.get(position).getQuote_id()));
//            }
//        });
//        holder.like_lay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int fav = database.get_like_quotes(quotes_lists.get(position).getQuote_id());
//
//                if (fav == 0) {
//
//                    holder.like_bt.playAnimation();
//                    holder.like_bt.setChecked(true);
//                    holder.tv_like.setText("Liked");
//
//                    database.liked_list_update(quotes_lists.get(position).getQuote_id(), 1);
//                } else {
//                    holder.like_bt.setChecked(false);
//                    holder.tv_like.setText("Like");
//                    database.liked_list_update(quotes_lists.get(position).getQuote_id(), 0);
//                }
//            }
//        });
        holder.like_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fav = database.get_like_quotes(quotes_lists.get(position).getQuote_id());
                if (fav == 0) {

                    holder.like_bt.playAnimation();
                    holder.like_bt.setChecked(true);
                    holder.tv_like.setText("Liked");

                    database.liked_list_update(quotes_lists.get(position).getQuote_id(), 1);
                } else {
                    holder.like_bt.setChecked(false);
                    holder.tv_like.setText("Like");
                    database.liked_list_update(quotes_lists.get(position).getQuote_id(), 0);
                }
            }
        });
//        holder.save_lay.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//               /* ColorDrawable viewColor = (ColorDrawable) holder.cardView.getBackground();
//                int save_colorId = viewColor.getColor();
//*/
//                if (prefs.getString("sound","").equals("on")){
//                    mediaPlayer.start();
//                }
//                /*save_al_compare = getArrayList("saveImgArrayList");
//                if (save_al_compare == null) {
//                    save_al_compare = new ArrayList<>();
//
//                }
//                if (save_al_compare.size() == 0) {
//                    loadView(holder.cardView,quotes_lists.get(position).getQuote_id());
//                    save_al_compare.add(quotes_lists.get(position).getQuote_id());
//                    saveArrayList(save_al_compare, "saveImgArrayList");
//                    holder.save_img.setImageResource(R.drawable.ic_checked);
//                    holder.tv_save.setText("Saved");
//
//                } else {
//                    for (int i = 0; i < save_al_compare.size(); i++) {
//                        if (!save_al_compare.get(i).equals(quotes_lists.get(position).getQuote_id())) {
//                            loadView(holder.cardView,quotes_lists.get(position).getQuote_id());
//
//                            holder.save_img.setImageResource(R.drawable.ic_checked);
//                            holder.tv_save.setText("Saved");
//                            break;
//                        }
//                    }
//                }*/
//                loadView(holder.cardView,quotes_lists.get(position).getQuote_id());
//                Log.e("save_image","save in progress");
//            }
//        });
        holder.copy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(q_activity, "Copy to clipboard", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) q_activity.getSystemService(q_activity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy Quote", quotes_lists.get(position).getContent());
                clipboard.setPrimaryClip(clip);
            }
        });
        holder.share_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(q_activity, "share", Toast.LENGTH_SHORT).show();
                anInterface.shareQuote(quotes_lists.get(position).getContent(), holder.share_lay, holder.cardView);
            }
        });
    }

    public ArrayList<Integer> sendSaveArrayList() {
        return save_al;
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void loadView(CardView cardView, Integer quot_id) {
//        View view = cardView;
//
//        try {
//            Log.e("loadView", "true");
//            view.setDrawingCacheEnabled(true);
//            Bitmap bitmap = getBitmapFromView(view);
//            view.setDrawingCacheEnabled(false);
//
//            Log.e("loadView", "false");
//
//            File imageFile = createImageFile(cardView,quot_id);
//            Log.e("save_image","image saved");
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            Log.e("compress", "compress");
//            outputStream.flush();
//            outputStream.close();
//
//            MediaScannerConnection.scanFile(q_activity, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//                /*
//                 *   (non-Javadoc)
//                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
//                 */
//                public void onScanCompleted(String path, Uri uri) {
//                    Log.i("ExternalStorage", "Scanned " + path + ":");
//                    Log.i("ExternalStorage", "-> uri=" + uri);
//                }
//            });
//            Toast.makeText(q_activity, "Saved", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//
//            Log.e("Exception", "" + e.toString());
//            e.printStackTrace();
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public File createImageFile(CardView cardView, Integer quot_id) throws IOException {
//        // Create an image file name
//        Log.e("createImageFile", "createImageFile");
//        File storageDir = new File(Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Best Quotes/");
//        if (!storageDir.exists()) {
//            Log.e("mkdirs", "mkdirs========" + storageDir.getAbsolutePath());
//            storageDir.mkdirs();
//        }
//        // Date currentDate = new Date(System.currentTimeMillis());
//        String time = "" + System.currentTimeMillis();
//        Log.e("save_image", "file path create");
//
//        ArrayList<ColorId_and_QuotId> colorCompareList = new ArrayList<>();
////        colorCompareList=getArrayList("colorId_and_quoteId");
//        String img_name = "";
//        for (int i = 0; i < colorCompareList.size(); i++) {
//            if (colorCompareList.get(i).getQuot_id() == quot_id) {
//                img_name = String.valueOf(colorCompareList.get(i).getQuot_id() + colorCompareList.get(i).getQuot_id());
//                Log.e("save_img_name", String.valueOf(colorCompareList.get(i).getQuot_id() + colorCompareList.get(i).getQuot_id()));
//            }
//        }
//
//        File image = new File(storageDir, img_name + ".jpeg");
//        Log.e("image", "image===========" + image.getAbsolutePath());
//        return image;
//    }

//    private Bitmap getBitmapFromView(View view) {
//        //Define a bitmap with the same size as the view
//        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        //Bind a canvas to it
//        Canvas canvas = new Canvas(returnedBitmap);
//        //Get the view's background
//        Drawable bgDrawable = view.getBackground();
//        if (bgDrawable != null) {
//            //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        } else {
//            //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE);
//        }
//        // draw the view on the canvas
//        view.draw(canvas);
//        //return the bitmap
//        return returnedBitmap;
//    }
//
//    ArrayList<ColorId_and_QuotId> colorId_and_quoteId = new ArrayList<>();
//    ArrayList<Integer> integerArrayList = new ArrayList<>();
//    int[] arr = new int[7947];
//
//    public int getRandomColorCode(Integer quot_id) {
//        Random random = new Random();
//        int zx = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
//
//        if (colorId_and_quoteId.size() == 0) {
//            ColorId_and_QuotId q = new ColorId_and_QuotId(quot_id, zx);
//            colorId_and_quoteId.add(q);
//        }
//        Boolean b = false;
//        for (int i = 0; i < colorId_and_quoteId.size(); i++) {
//            if (colorId_and_quoteId.get(i).getQuot_id() == quot_id) {
////                colorId_and_quoteId.get(i).setColor_id(zx);
//                b = true;
//                break;
//            }
//        }
//        if (b == false) {
//            ColorId_and_QuotId q = new ColorId_and_QuotId(quot_id, zx);
//            colorId_and_quoteId.add(q);
//        }
////        saveArrayList(colorId_and_quoteId, "colorId_and_quoteId");
//        return zx;
//    }

//    public void saveArrayList(ArrayList<ColorId_and_QuotId> list, String key) {
//        Log.e("saveArrayList", "" + list.size());
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(q_activity);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString(key, json);
//        editor.apply();
//    }

//    public ArrayList<ColorId_and_QuotId> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(q_activity);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<ColorId_and_QuotId>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }
    @Override
    public int getItemCount() {
        return quotes_lists.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {
        TextView tv_content;
        CardView cardView;
        LinearLayout like_lay, save_lay, copy_lay, share_lay;
        TextView tv_like, tv_save;
        SparkButton like_bt;
        ImageView save_img;

        public MyClass(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.quotes_text);
            cardView = itemView.findViewById(R.id.quotes_cardView);
            like_lay = itemView.findViewById(R.id.like_lay);
            copy_lay = itemView.findViewById(R.id.copy_lay);
            share_lay = itemView.findViewById(R.id.share_lay);
            tv_like = itemView.findViewById(R.id.tv_like);
            like_bt = itemView.findViewById(R.id.like_bt);
//            tv_save = itemView.findViewById(R.id.tv_save);
//            save_lay = itemView.findViewById(R.id.save_lay);
//            save_img = itemView.findViewById(R.id.save_img);
        }
    }

}
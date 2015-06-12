package com.recyclegridview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Utils {

	public static HashMap<String, Bitmap> cache = new HashMap<String, Bitmap> ();

	public static Bitmap getBitmapScale(Bitmap bitmap) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		try {
			return Bitmap.createScaledBitmap (bitmap, (int) width, (int) height, true);
		} finally {
			bitmap.recycle();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static ArrayList<AlbumsModel> getAllDirectoriesWithImages(
			Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		cursor.moveToFirst();
		int size = cursor.getCount();

		TreeSet<String> folderPathList = new TreeSet<String> ();
		ArrayList<AlbumsModel> albumsModels = new ArrayList<AlbumsModel> ();
		HashMap<String, AlbumsModel> map = new HashMap<String, AlbumsModel> ();

		String imgPath, folderPath;
		AlbumsModel tempAlbumsModel;
		for (int i = 0; i < size; i++) {
			imgPath = cursor.getString(0).trim();
			folderPath = imgPath.substring(0, imgPath.lastIndexOf("/"));
			if (folderPathList.add(folderPath)) {
				AlbumsModel gm = new AlbumsModel ();
                String folderName = gm.getFolderName ();
                String folderImagePath = gm.getFolderName ();

                gm.folderName = folderPath.substring(
						folderPath.lastIndexOf("/") + 1, folderPath.length());
				gm.folderImages.add(imgPath);
				gm.folderImagePath = imgPath;
				albumsModels.add(gm);
				map.put(folderPath, gm);
			} else if (folderPathList.contains(folderPath)) {
				tempAlbumsModel = map.get(folderPath);
				tempAlbumsModel.folderImages.add(imgPath);
			}
			cursor.moveToNext();
		}
		return albumsModels;
	}

	public static ArrayList<String> getAllImagesOfFolder(String directoryPath) {
		File directory = new File (directoryPath);
		ArrayList<String> pathList = new ArrayList<String> ();
		try {
			if (directory.exists() && directory.isDirectory()) {
				String[] fileList = directory.list();
				for (String file : fileList) {
					if (isImage(file)
							&& new File (directoryPath + file).length() > 0
							&& !file.startsWith(".")) {
						pathList.add(directoryPath + file);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathList;
	}

	public static boolean isImage(String file) {
		try {
			String extension = file.substring(file.lastIndexOf("."),
					file.length());
			if (extension != null) {
				if (extension.equalsIgnoreCase(".jpeg")
						|| extension.equalsIgnoreCase(".jpg")
						|| extension.equalsIgnoreCase(".png")
						|| extension.equalsIgnoreCase(".gif")
						|| extension.equalsIgnoreCase(".tiff")
						|| extension.equalsIgnoreCase(".bmp")) {
					return true;
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return false;
	}

    public static void writeImageFile (String file_name, File f1, Context context) {
        try {
            Log.d ("file_name--", " " + file_name);

            // File f2 = new File(Environment.getExternalStorageDirectory().toString()+"/Folder/"+file_name+".apk");
            // f2.createNewFile();

            File f2 = new File (Environment.getExternalStorageDirectory ().toString () + "/ZappShare" + "/SharedImages");
            f2.mkdirs ();
            f2 = new File (f2.getPath () + "/" + file_name + ".jpg");
            f2.createNewFile ();

            InputStream in = new FileInputStream (f1);

            OutputStream out = new FileOutputStream (f2);

            byte[] buf = new byte[ 1024 ];
            int len;
            while ((len = in.read (buf)) > 0) {
                out.write (buf, 0, len);
            }
            in.close ();
            out.close ();
            Toast.makeText (context, "File copied", Toast.LENGTH_SHORT).show ();

        } catch (FileNotFoundException ex) {
            System.out.println (ex.getMessage () + " in the specified directory.");
        } catch (IOException e) {
            System.out.println (e.getMessage ());
        }
    }
    public static File copyFileToExternal (Context context, String fileName) {
        File file = null;
        String newPath = Environment.getExternalStorageState()+"/ZappShare" + "/SharedImages";
        try {
            File f = new File(newPath);
            f.mkdirs();
            FileInputStream fin = context.openFileInput (fileName);
            FileOutputStream fos = new FileOutputStream(newPath + fileName);
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = fin.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fin.close();
            fos.close();
            file = new File(newPath + fileName);
            if (file.exists())
                return file;
        } catch (Exception e) {

        }
        return null;
    }

    public static void appBackupAndSend ( Context context,ArrayList<File> fileList) {
        try {
            //            Log.d ("file_name--", " " + file_name);

            // File f2 = new File(Environment.getExternalStorageDirectory().toString()+"/Folder/"+file_name+".apk");
            // f2.createNewFile();
            ArrayList<File> backupFiles = new ArrayList<File> ();

            for(File file: fileList) {
                File filePath = new File (Environment.getExternalStorageDirectory ().toString () + "/ZappBackup");
                filePath.mkdirs ();
                filePath = new File (filePath.getPath () + "/" + file.getName () + ".apk");
                backupFiles.add(filePath);
                filePath.createNewFile ();

                InputStream in = new FileInputStream (file);

                OutputStream out = new FileOutputStream (filePath);

                byte[] buf = new byte[ 1024 ];
                int len;
                while ((len = in.read (buf)) > 0) {
                    out.write (buf, 0, len);
                }
                in.close ();
                out.close ();
                Toast.makeText (context, "File copied", Toast.LENGTH_SHORT).show ();
            }
            Intent shareIntent = new Intent (Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType ("application/vnd.android.package-archive");
            shareIntent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ArrayList<Uri> uriFiles = new ArrayList<Uri> ();

            for(File file:backupFiles) {
                uriFiles.add (Uri.fromFile (file));
            }
            shareIntent.putParcelableArrayListExtra (Intent.EXTRA_STREAM, uriFiles);

            try {
                context.startActivity (Intent.createChooser (shareIntent, "Share via"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText (context, "There are no share applications installed.", Toast.LENGTH_SHORT).show ();
            }

        } catch (FileNotFoundException ex) {
            System.out.println (ex.getMessage () + " in the specified directory.");
        } catch (IOException e) {
            System.out.println (e.getMessage ());
        }
    }

    public static void createCachedFile (Context context, String key, ArrayList<Uri> fileName) throws IOException {

        try {
            String tempFile = null;
            for (Uri file : fileName) {
                FileOutputStream fos = context.openFileOutput (key, Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream (fos);
                oos.writeObject (fileName);
                oos.close ();
                fos.close ();

            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }


    public static Object readCachedFile (Context context, String key) throws IOException, ClassNotFoundException {
        Object object = null;
        try {
            FileInputStream fis = context.openFileInput (key);
            ObjectInputStream ois = new ObjectInputStream (fis);
            object = ois.readObject ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        return object;
    }
}

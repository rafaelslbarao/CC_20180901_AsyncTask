package br.com.datamob.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private static final int THREAD_SLEEP = 100;


    private Handler handler;
    private TextView tvProgresso;
    private Button btThread;
    private Button btAsyncTask;
    private ProgressBar pbProgresso;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaComponenetes();
    }

    private void inicializaComponenetes()
    {
        handler = new Handler();
        tvProgresso = findViewById(R.id.tvProgresso);
        btThread = findViewById(R.id.btThread);
        btAsyncTask = findViewById(R.id.btAsyncTask);
        pbProgresso = findViewById(R.id.pbProgresso);

        btAsyncTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new TesteAsyncTask().execute("Parametro1", "Parametro2");
            }
        });

        btThread.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new TesteThread("Parametro1", "Parametro2").start();
            }
        });
    }

    private class TesteAsyncTask extends AsyncTask<String, Integer, Boolean>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            tvProgresso.setText(R.string.IniciandoAsyncTask);
        }

        @Override
        protected Boolean doInBackground(String... parametros)
        {
            try
            {
                Thread.sleep(2000);
                for (int i = 1; i <= 100; i++)
                {

                    Thread.sleep(THREAD_SLEEP);
                    publishProgress(i);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            if (values != null && values.length > 0)
            {
                tvProgresso.setText(values[0].toString());
                pbProgresso.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean resultado)
        {
            super.onPostExecute(resultado);
            tvProgresso.setText(R.string.FinalizadoAsyncTask);
        }
    }

    private class TesteThread extends Thread
    {
        private String[] parametros;

        public TesteThread(String... parametros)
        {
            this.parametros = parametros;
            onPreExecute();
        }

        protected void onPreExecute()
        {
            tvProgresso.setText(R.string.IniciandoThread);
        }

        @Override
        public void run()
        {
            super.run();
            try
            {
                Thread.sleep(2000);
                for (int i = 1; i <= 100; i++)
                {
                    final int progresso = i;
                    Thread.sleep(THREAD_SLEEP);
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            onProgressUpdate(progresso);
                        }
                    });
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    onPostExecute(true);
                }
            });
        }

        protected void onProgressUpdate(Integer... values)
        {
            if (values != null && values.length > 0)
            {
                tvProgresso.setText(values[0].toString());
                pbProgresso.setProgress(values[0]);
            }
        }

        protected void onPostExecute(Boolean resultado)
        {
            tvProgresso.setText(R.string.FinalizadoThread);
        }
    }
}

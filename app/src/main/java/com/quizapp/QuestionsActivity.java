package com.quizapp;

import static android.media.CamcorderProfile.get;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quizapp.Models.QuestionsModel;
import com.quizapp.databinding.ActivityQuestionsBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
ActivityQuestionsBinding binding;
FirebaseDatabase database;
ArrayList<QuestionsModel>list;
private int count=0;
private  int position=0;
private int correctAnsw=0;
private int wrongAnsw=0;
private long questionTime=0;
private long timeLeft;
private String catId,subCatId;
CountDownTimer timer;
private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_questions);
        catId=getIntent().getStringExtra("catId");
        subCatId=getIntent().getStringExtra("subCatId");
        database=FirebaseDatabase.getInstance();
        list=new ArrayList<>();
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        startTimer();
        database.getReference().child("categories").child(catId).child("subCategories")
                .child(subCatId).child("questions").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           timer.start();
                           for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                               QuestionsModel model=dataSnapshot.getValue(QuestionsModel.class);
                               model.setKey(dataSnapshot.getKey());
                               list.add(model);
                               loadingDialog.dismiss();
                           }
                           if(list.size()>0){
                               for(int i=0;i<4;i++){
                                  binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          checkAnswer((Button)view);
                                      }
                                  });
                               }
                           }

                           playAnimation(binding.question,0,list.get(position).getQuestion());
                           binding.btnNext.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   enableOption(true);
                                   position++;
                                   if(position==list.size()){
                                       timer.cancel();
                                       long totalTime=questionTime*60*100;
                                       Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                                       intent.putExtra("time_taken",totalTime-timeLeft);
                                       intent.putExtra("correct",correctAnsw);
                                       intent.putExtra("wrong",wrongAnsw);
                                       intent.putExtra("total_questions",list.size());
                                       startActivity(intent);
                                       finish();
                                       return;
                                   }
                                   count=0;
                                   playAnimation(binding.question,0,list.get(position).getQuestion());
                               }
                           });
                           binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   timer.cancel();
                                   long totalTime=questionTime*60*100;
                                   Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                                   intent.putExtra("time_taken",totalTime-timeLeft);
                                   intent.putExtra("correct",correctAnsw);
                                   intent.putExtra("wrong",wrongAnsw);
                                   intent.putExtra("total_questions",list.size());
                                   startActivity(intent);
                               }
                           });

                       }
                       else {
                           loadingDialog.dismiss();
                           Toast.makeText(QuestionsActivity.this,"questions does not exists",Toast.LENGTH_SHORT).show();
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void startTimer() {
        long time=questionTime*60*100;
        timer=new CountDownTimer(time+1000,1000) {
            @Override
            public void onTick(long l) {
                timeLeft=l;
                String remainingTime=String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                binding.time.setText(remainingTime);

            }

            @Override
            public void onFinish() {

                long totalTime=questionTime*60*100;
                Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                intent.putExtra("time_taken",totalTime-timeLeft);
                intent.putExtra("correct",correctAnsw);
                intent.putExtra("wrong",wrongAnsw);
                intent.putExtra("total_questions",list.size());
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        };
    }

    private void enableOption(boolean enable) {
        for (int i=0;i<4;i++){
            binding.optionContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_option_bg);
            }
        }


    }

    private void playAnimation(View view, int value,final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                      if(value==0 && count<4){
                          String option="";
                          if(count==0){
                              option=list.get(position).getOptionA();
                          }
                          else if(count==1){
                              option=list.get(position).getOptionB();
                          }
                          else if(count==2){
                              option=list.get(position).getOptionC();
                          }
                          else if(count==3){
                              option=list.get(position).getOptionD();
                          }
                          playAnimation(binding.optionContainer.getChildAt(count),0,option);
                          count++;
                      }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if(value==0){
                            try {
                                ((TextView)view).setText(data);
                                binding.questionCount.setText(position+1+"/"+list.size());

                            }
                            catch (Exception e){
                                ((Button)view).setText(data);

                            }
                            view.setTag(data);
                            playAnimation(view,1,data);
                        }

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });

    }

    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            correctAnsw++;
            selectedOption.setBackgroundResource(R.drawable.correct_option_bg);
        }
        else{
            wrongAnsw++;
            selectedOption.setBackgroundResource(R.drawable.wrong_option_bg);
            Button correctOption=(Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.correct_option_bg);
        }
    }
}
//import java.io.File;
//import java.util.Iterator;
//import java.util.List;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//
//import kotlin.Metadata;
//import kotlin.ResultKt;
//import kotlin.Unit;
//import kotlin.coroutines.Continuation;
//import kotlin.coroutines.intrinsics.IntrinsicsKt;
//import kotlin.coroutines.jvm.internal.ContinuationImpl;
//import kotlin.jvm.internal.Intrinsics;
//import kotlin.text.StringsKt;
//import kotlinx.coroutines.DelayKt;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//@Metadata(
//    mv = {1, 1, 18},
//    bv = {1, 0, 3},
//    k = 1,
//    d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J!\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"},
//    d2 = {"LBeats;", "", "()V", "playBeats", "", "beats", "", "file", "playBeatsWithSuspend", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "playSound", "head-first-kotlin"}
//)
//public final class Beats {
//    public final void playBeats(@NotNull String beats, @NotNull String file) {
//        Intrinsics.checkParameterIsNotNull(beats, "beats");
//        Intrinsics.checkParameterIsNotNull(file, "file");
//        List parts = StringsKt.split$default((CharSequence) beats, new String[]{"x"}, false, 0, 6, (Object) null);
//        int count = 0;
//        Iterator var6 = parts.iterator();
//
//        while (var6.hasNext()) {
//            String part = (String) var6.next();
//            count += part.length() + 1;
//            if (Intrinsics.areEqual(part, "")) {
//                this.playSound(file);
//            } else {
//                Thread.sleep((long) 100 * ((long) part.length() + 1L));
//                if (count < beats.length()) {
//                    this.playSound(file);
//                }
//            }
//        }
//
//    }
//
//    @Nullable
//    public final Object playBeatsWithSuspend(@NotNull String beats, @NotNull String file, @NotNull Continuation var3) {
//        Object $continuation;
//        // The Label variable will be passed around as the “next code block pointer”
//        // to resume. The Kotlin compiler at compilation time will figure out all
//        // the suspend call sites and reorganize the code (in a generic code generation way)
//        // so that each number will map to a suspend call site
//        label44:
//        {
//            if (var3 instanceof <undefinedtype >){
//            $continuation = ( < undefinedtype >)var3;
//            if (((( < undefinedtype >) $continuation).label & Integer.MIN_VALUE) !=0){
//                (( < undefinedtype >) $continuation).label -= Integer.MIN_VALUE;
//                break label44;
//            }
//        }
//
//            $continuation = new ContinuationImpl(var3) {
//                // $FF: synthetic field
//                Object result;
//                int label;
//                Object L$0;
//                Object L$1;
//                Object L$2;
//                Object L$3;
//                int I$0;
//
//                @Nullable
//                public final Object invokeSuspend(@NotNull Object $result) {
//                    this.result = $result;
//                    this.label |= Integer.MIN_VALUE;
//                    return Beats.this.playBeatsWithSuspend((String) null, (String) null, this);
//                }
//            };
//        }
//
//        Object $result = (( < undefinedtype >) $continuation).result;
//
//        // Notice this variable:  IntrinsicsKt.getCOROUTINE_SUSPENDED(). We can see that a suspend function can either return IntrinsicsKt.getCOROUTINE_SUSPENDED()
//        // or the actual result of the function. If the return value is the former case, we know the suspend function we are calling is not ready,
//        // and we need to suspend the current function and return
//        Object var10 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
//        int count;
//        Iterator var7;
//        switch ((( < undefinedtype >) $continuation).label){
//            case 0:
//                ResultKt.throwOnFailure($result);
//                StringBuilder var10000 = (new StringBuilder()).append("Playing using thread ");
//                Thread var10001 = Thread.currentThread();
//                Intrinsics.checkExpressionValueIsNotNull(var10001, "Thread.currentThread()");
//                String var4 = var10000.append(var10001.getName()).toString();
//                System.out.println(var4);
//                List parts = StringsKt.split$default((CharSequence) beats, new String[]{"x"}, false, 0, 6, (Object) null);
//                count = 0;
//                var7 = parts.iterator();
//                break;
//            case 1:
//                count = (( < undefinedtype >) $continuation).I$0;
//                var7 = (Iterator) (( < undefinedtype >) $continuation).L$3;
//                file = (String) (( < undefinedtype >) $continuation).L$2;
//                beats = (String) (( < undefinedtype >) $continuation).L$1;
//                this = (Beats) (( < undefinedtype >) $continuation).L$0;
//                ResultKt.throwOnFailure($result);
//                if (count < beats.length()) {
//                    this.playSound(file);
//                }
//                break;
//            default:
//                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
//        }
//
//        while (var7.hasNext()) {
//            String part = (String) var7.next();
//            count += part.length() + 1;
//            if (Intrinsics.areEqual(part, "")) {
//                this.playSound(file);
//            } else {
//                long var12 = (long) 100 * ((long) part.length() + 1L);
//                (( < undefinedtype >) $continuation).L$0 = this;
//                (( < undefinedtype >) $continuation).L$1 = beats;
//                (( < undefinedtype >) $continuation).L$2 = file;
//                (( < undefinedtype >) $continuation).L$3 = var7;
//                (( < undefinedtype >) $continuation).I$0 = count;
//                (( < undefinedtype >) $continuation).label = 1;
//                if (DelayKt.delay(var12, (Continuation) $continuation) == var10) {
//                    return var10;
//                }
//
//                if (count < beats.length()) {
//                    this.playSound(file);
//                }
//            }
//        }
//
//        return Unit.INSTANCE;
//    }
//
//    public final void playSound(@NotNull String file) {
//        Intrinsics.checkParameterIsNotNull(file, "file");
//        Clip clip = AudioSystem.getClip();
//        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
//        clip.open(audioInputStream);
//        clip.start();
//    }
//}

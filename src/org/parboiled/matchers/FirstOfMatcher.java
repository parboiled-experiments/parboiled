/*
 * Copyright (C) 2009 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.parboiled.matchers;

import org.jetbrains.annotations.NotNull;
import org.parboiled.support.Characters;
import org.parboiled.support.Checks;
import org.parboiled.support.Chars;
import org.parboiled.common.StringUtils;
import org.parboiled.common.Utils;
import org.parboiled.Rule;
import org.parboiled.MatcherContext;

public class FirstOfMatcher<V> extends AbstractMatcher<V> {

    public FirstOfMatcher(@NotNull Rule[] subRules) {
        super(subRules);
    }

    public boolean match(@NotNull MatcherContext<V> context, boolean enforced) {
        for (int i = 0; i < getChildren().size(); i++) {
            Matcher<V> matcher = getChildren().get(i);
            boolean matched = context.runMatcher(matcher, false);
            if (matched) {
                context.createNode();
                return true;
            }
        }
        return false;
    }

    public Characters getStarterChars() {
        Characters chars = Characters.NONE;
        for (Matcher matcher : getChildren()) {
            chars = chars.add(matcher.getStarterChars());
            Checks.ensure(!chars.contains(Chars.EMPTY),
                    "Rule '{}' allows empty matches, unlikely to be correct as a sub rule of an FirstOf-Rule");
        }
        return chars;
    }

    public String getExpectedString() {
        int count = getChildren().size();
        if (count == 0) return "";
        if (count == 1) return getChildren().get(0).toString();
        return StringUtils.join(Utils.subarray(getChildren().toArray(), 0, count - 1), ", ") +
                " or " + getChildren().get(count - 1);
    }

}